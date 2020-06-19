package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal fun AppScope.rosterItemStatesFlow(): Flow<List<Roster.Item>> {
    val items = mutableMapOf<Address, Set<Roster.Item>>()

    return sessionStore.newSessionsFlow().flatMapMerge { session ->
        session.rosterItemStatesFlow().map { states ->
            session.address to states
        }
    }.onEach { changed ->
        items += changed
    }.bufferedThrottle(100).map {
        items.values.flatten()
    }
}

private fun SessionScope.rosterItemStatesFlow(): Flow<Set<Roster.Item>> {
    val jobs = mutableMapOf<Address, Job>()
    val items = mutableMapOf<Address, Roster.Item>()

    return channelFlow {
        chatRepo.flowList().map { chats ->

            chats.map(Chat::address).toSet()

        }.collect { current: Set<Address> ->

            // Cancel jobs of removed chats
            jobs.minus(current).map { (address, job) ->
                job.cancel()
                items -= address
                jobs -= address
            }

            // Observe new chat changes
            current.minus(jobs.keys).map { address ->
                jobs += (address to launch {
                    rosterItemStatesFlow(address).collect { state ->
                        items.apply {
                            put(address, state)
                            send(values.toSet())
                        }
                    }
                })
            }
        }
    }
}


private fun SessionScope.Store.newSessionsFlow(): Flow<SessionScope> {
    var previous = emptyMap<Address, SessionScope>()
    return changesFlow().flatMapConcat { current: Map<Address, SessionScope> ->
        val new = current - previous.keys
        previous = current
        new.values.asFlow()
    }
}
