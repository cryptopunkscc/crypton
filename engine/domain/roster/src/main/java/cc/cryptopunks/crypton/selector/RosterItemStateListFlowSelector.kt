package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Session
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

internal class RosterItemStateListFlowSelector(
    private val sessionStore: Session.Store,
    private val createRosterItemStateFlowSelector: RosterItemStateFlowSelector.Factory
) {
    operator fun invoke(): Flow<List<Roster.Item.Chat>> =
        mutableMapOf<Address, Set<Roster.Item.Chat>>().let { currentItems ->
            sessionStore.newSessionsFlow().flatMapMerge { session ->
                session.rosterItemStateFlow().map { states ->
                    session.address to states
                }
            }.onEach { changed ->
                currentItems += changed
            }.bufferedThrottle(100).map {
                currentItems.values.flatten()
            }
        }

    private fun Session.Store.newSessionsFlow(): Flow<Session> {
        var previous = emptyMap<Address, Session>()
        return changesFlow().flatMapConcat { current ->
            val new = current - previous.keys
            previous = current
            new.values.asFlow()
        }
    }

    private fun Session.rosterItemStateFlow(): Flow<Set<Roster.Item.Chat>> {
        val chatJobs = mutableMapOf<Address, Job>()
        val chatItems = mutableMapOf<Address, Roster.Item.Chat>()
        val rosterItemStateFlow = createRosterItemStateFlowSelector(this)

        return channelFlow {
            chatRepo.flowList().collect { currentChats ->

                val currentAddresses: Set<Address> = currentChats.map(Chat::address).toSet()

                // Cancel jobs of removed chats
                chatJobs.minus(currentAddresses).map { (address, job) ->
                    job.cancel()
                    chatItems -= address
                    chatJobs -= address
                }

                // Observe new chat changes
                currentAddresses.minus(chatJobs.keys).map { address ->
                    chatJobs += (address to scope.launch {
                        rosterItemStateFlow(address).collect { state ->
                            chatItems.apply {
                                put(address, state)
                                send(values.toSet())
                            }
                        }
                    })
                }
            }
        }
    }
}
