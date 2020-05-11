package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.MockSessionRepo
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.mock.net.MockConnectionFactory
import cc.cryptopunks.crypton.util.Log
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RosterListFlowSelectorTest {

    private val domain = "test.mock"
    private val addresses = (0..3).map { Address("test$it", domain) }

    private val sessionRepos = (0..1).map { MockSessionRepo() }

    private val createConnection = MockConnectionFactory()

    private val sessionScopes = (0..1).map { Session.Scope() }
    private val connections = (0..1).map {
        createConnection(
            Connection.Config(
                address = addresses[it],
                scope = sessionScopes[it]
            )
        )
    }

    private val sessions = (0..1).map {
        Session(
            address = addresses[it],
            scope = sessionScopes[it],
            sessionRepo = sessionRepos[it],
            connection = connections[it]
        )
    }

    private val chats = (0..1).map {
        Chat(
            title = addresses[it + 2].toString(),
            address = addresses[it + 2],
            account = addresses[it],
            users = listOf(it, it + 2).map { User(addresses[it]) }
        )
    }

    private val messages = (0..1).map {
        Message(
            id = it.toString(),
            text = "text$it",
            chatAddress = chats[it].address,
            to = Resource(addresses[it]),
            from = Resource(addresses[it + 2]),
            status = Message.Status.Received
        )
    }

    @Before
    fun setUp() {
        Log.init(mockk(relaxed = true))
    }

    @Test
    operator fun invoke() = runBlocking {
        // given
        val sessionStore = Session.Store()
        val userPresenceStore = UserPresence.Store()

        val selector = RosterItemStateListFlowSelector(
            sessionStore = sessionStore,
            createRosterItemStateFlowSelector = RosterItemStateFlowSelector.Factory(
                presenceFlow = PresenceFlowSelector(
                    userPresenceStore = userPresenceStore
                )
            )
        )

        // when
        sessionStore reduce {
            sessions.map { it.address to it }.toMap()
        }
        userPresenceStore reduce {
            (2..3).map {
                addresses[it] to Presence(Presence.Status.Available)
            }.toMap()
        }
        (0..1).forEach {
            sessionRepos[it].chatRepo.store reduce {
                mapOf(chats[it].address to chats[it])
            }
            sessionRepos[it].messageRepo.store reduce {
                mapOf(messages[it].id to messages[it])
            }
        }

        // then
        val result = withTimeout(5000) {
            selector().first().toSet()
        }
        result.forEach(::println)
        Assert.assertEquals(
            setOf(
                Roster.Item.State(
                    title = addresses[2].toString(),
                    unreadMessagesCount = 1,
                    message = messages[0],
                    presence = Presence.Status.Available,
                    letter = 't'
                ),
                Roster.Item.State(
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
