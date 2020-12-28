package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.chatRepo
import cc.cryptopunks.crypton.context.sessions
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

internal fun RootScope.rosterItemStatesFlow(): Flow<List<Roster.Item>> {
    val items = mutableMapOf<Address, Set<Roster.Item>>()

    return sessions.changesFlow().flatMapMerge { sessions ->
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
        session.sessionsRosterItemStatesFlow().map { states ->
            session.account.address to states
        }
    }.asFlow().flattenMerge()


private fun SessionScope.sessionsRosterItemStatesFlow(): Flow<Set<Roster.Item>> {
    val context = coroutineContext
    val chatRepo = chatRepo
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
                jobs += (address to launch(context) {
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
