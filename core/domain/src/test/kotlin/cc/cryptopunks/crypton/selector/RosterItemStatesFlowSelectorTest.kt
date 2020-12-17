package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.presenceStore
import cc.cryptopunks.crypton.context.sessions
import cc.cryptopunks.crypton.mock.MockConnectionFactory
import cc.cryptopunks.crypton.mock.MockSessionRepo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class RosterItemStatesFlowSelectorTest {

    @Before
    fun setUp() {
//        Log.init(mockk(relaxed = true))
    }

    @Test
    @Ignore("TODO fix or remove")
    operator fun invoke() = runBlocking {
        // given
        val domain = "test.mock"
        val addresses = (0..3).map { Address("test$it", domain) }

        val sessionRepos = (0..1).map { MockSessionRepo() }

        val createConnection =
            MockConnectionFactory()

        val connections = (0..1).map {
            createConnection(
                Connection.Config(
                    account = addresses[it],
                    scope = this
                )
            )
        }

        val sessionStore = SessionScope.Store()
        val presenceStore = Presence.Store()

        val rootScope = mockk<RootScope> {
            every { this@mockk.sessions } returns sessionStore
        }

        val sessions = (0..1).map {
            TODO()
//            SessionModule(
//                cryptonContext(
//                    Account.Name(addresses[it]),
//                    sessionRepos[it].context(),
//                    connections[it].context(),
//                    rootScope,
//                    rootScope.asDep(),
//                )
//            )
        }

        val chats = (0..1).map {
            Chat(
                title = addresses[it + 2].toString(),
                address = addresses[it + 2],
                account = addresses[it],
                users = listOf(it, it + 2).map { addresses[it] }
            )
        }

        val messages = (0..1).map {
            Message(
                id = it.toString(),
                body = "text$it",
                chat = chats[it].address,
                to = Resource(addresses[it]),
                from = Resource(addresses[it + 2]),
                status = Message.Status.Received
            )
        }


        // when
        sessionStore.reduce {
            sessions.associateBy { it.account.address }
        }
        sessions.forEach {
            it.presenceStore.reduce {
                (2..3).associate { i ->
                    addresses[i] to Presence(
                        Resource(addresses[i]),
                        Presence.Status.Available
                    )
                }
            }
        }
        (0..1).forEach {
            sessionRepos[it].run {
                chatRepo.store.reduce {
                    mapOf(chats[it].address to chats[it])
                }
                messageRepo.store.reduce {
                    mapOf(messages[it].id to messages[it])
                }
            }
        }

        // then
        val result = withTimeout(5000) {
            rootScope.rosterItemStatesFlow().first().toSet()
        }
        result.forEach(::println)
        Assert.assertEquals(
            setOf(
                Roster.Item(
                    account = addresses[0],
                    title = addresses[2].toString(),
                    unreadMessagesCount = 1,
                    message = messages[0],
                    presence = Presence.Status.Available,
                    letter = 't'
                ),
                Roster.Item(
                    account = addresses[1],
                    title = addresses[3].toString(),
                    unreadMessagesCount = 1,
                    message = messages[1],
                    presence = Presence.Status.Available,
                    letter = 't'
                )
            ),
            result
        )
    }
}

