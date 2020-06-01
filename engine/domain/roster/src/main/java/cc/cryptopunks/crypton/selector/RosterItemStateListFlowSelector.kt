package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class RosterItemStateListFlowSelector(
    private val sessionStore: Session.Store,
    private val createRosterItemStateFlowSelector: RosterItemStateFlowSelector.Factory
) {
    operator fun invoke(): Flow<List<Roster.Item.State>> =
        mutableMapOf<Address, Set<Roster.Item.State>>().let { currentItems ->
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

    private fun Session.rosterItemStateFlow(): Flow<Set<Roster.Item.State>> {
        val chatJobs = mutableMapOf<Address, Job>()
        val chatItems = mutableMapOf<Address, Roster.Item.State>()
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
