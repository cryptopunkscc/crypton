package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal fun AppScope.rosterItemStatesFlow(): Flow<List<Roster.Item>> {
    val items = mutableMapOf<Address, Set<Roster.Item>>()

    return sessionStore.changesFlow().flatMapMerge { sessions ->
        flowOf(
            flowOf(Change(remove = items.keys - sessions.keys)),
            (sessions - items.keys).values.rosterItemStatesFlow().map { changed ->
                Change(update = changed)
            }
        ).flattenMerge()
    }.onEach {
        if (it.remove.isNotEmpty())
            items -= it.remove
        if (it.update != null)
            items += it.update
    }.bufferedThrottle(200).map {
        items.values.flatten()
    }
}

private data class Change(
    val remove: Set<Address> = emptySet(),
    val update: Pair<Address, Set<Roster.Item>>? = null
)

private fun Collection<SessionScope>.rosterItemStatesFlow(): Flow<Pair<Address, Set<Roster.Item>>> =
    map { session ->
        session.rosterItemStatesFlow().map { states ->
            session.address to states
        }
    }.asFlow().flattenMerge()


private fun SessionScope.rosterItemStatesFlow(): Flow<Set<Roster.Item>> {
    val jobs = mutableMapOf<Address, Job>()
    val items = mutableMapOf<Address, Roster.Item>()
    val sync = actor<suspend () -> Unit> {
        channel.consumeAsFlow().collect { it() }
    }
    return channelFlow {
        chatRepo.flowList().map { chats ->
            delay(50)
            chats.map(Chat::address).toSet()

        }.collect { current: Set<Address> ->

            // Cancel jobs of removed chats
            jobs.minus(current).map { (address, job) ->
                job.cancel()
                address
            }.also { addresses ->
                sync.send {
                    items -= addresses
                    jobs -= addresses
                }
            }

            // Observe new chat changes
            current.minus(jobs.keys).map { address ->
                jobs += (address to launch {
                    rosterItemStatesFlow(address).collect { state ->
                        sync.send {
                            items[address] = state
                            send(items.values.toSet())
                        }
                    }
                })
            }
        }
    }
}
