package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionModule
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.mock.MockConnectionFactory
import cc.cryptopunks.crypton.mock.MockSessionRepo
import cc.cryptopunks.crypton.util.Log
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RosterItemStatesFlowSelectorTest {

    @Before
    fun setUp() {
        Log.init(mockk(relaxed = true))
    }

    @Test
    operator fun invoke() = runBlocking {
        // given
        val domain = "test.mock"
        val addresses = (0..3).map { Address("test$it", domain) }

        val sessionRepos = (0..1).map { MockSessionRepo() }

        val createConnection =
            MockConnectionFactory()

        val sessionScopes = (0..1).map { SessionScope.Scope() }
        val connections = (0..1).map {
            createConnection(
                Connection.Config(
                    address = addresses[it],
                    scope = sessionScopes[it]
                )
            )
        }

        val sessionStore = SessionScope.Store()
        val presenceStore = Presence.Store()

        val appScope = mockk<AppScope> {
            every { this@mockk.sessionStore } returns sessionStore
        }

        val sessions = (0..1).map {
            SessionModule(
                address = addresses[it],
                sessionRepo = sessionRepos[it],
                connection = connections[it],
                appScope = appScope
            )
        }

        val chats = (0..1).map {
            Chat(
                title = addresses[it + 2].toString(),
                address = addresses[it + 2],
                account = addresses[it],
                users = listOf(it, it + 2).map { User(addresses[it]) }
            )
        }

        val messages = (0..1).map {
            Message(
                id = it.toString(),
                text = "text$it",
                chatAddress = chats[it].address,
                to = Resource(addresses[it]),
                from = Resource(addresses[it + 2]),
                status = Message.Status.Received
            )
        }


        // when
        sessionStore.reduce {
            sessions.associateBy { it.address }
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
            appScope.rosterItemStatesFlow().first().toSet()
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

