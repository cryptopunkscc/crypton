package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.CoroutineContext

class MockState(
    val account: Address
) : CoroutineScope {

    var omemoInitialized = false

    val defaults = Defaults(account)

    val contacts = ConflatedBroadcastChannel(defaults.contacts.toSet())

    val rosterEvents = Channel<Roster.Event>(Channel.BUFFERED)

    val messageEvents = Channel<Message.Incoming>(Channel.BUFFERED)

    val apiEvents = BroadcastChannel<Api.Event>(Channel.BUFFERED)

    val chatStore = Store<Map<Address, Chat>>(emptyMap())

    override val coroutineContext: CoroutineContext =
        SupervisorJob() + newSingleThreadContext(MockState::class.java.simpleName)

    operator fun invoke(block: suspend MockState.() -> Unit) {
        launch { block() }
    }

    class Defaults(
        account: Address
    ) {

        val resource = Resource(account, "mock")

        val contacts = listOf(
            "user1@cryptopunks.mock",
            "user2@cryptopunks.mock"
        ).map {
            address(it)
        }

        val chats = contacts.map {
            Chat(
                address = it,
                account = account,
                users = listOf(it, account)
            )
        }

        val messages = chats.map { chat ->
            listOf(
                "message1",
                "message2"
            ).map { messageText ->
                Message(
                    id = messageText + chat.address,
                    stanzaId = messageText + chat.address,
                    chat = chat.address,
                    from = Resource(chat.address),
                    to = Resource(account, "mock"),
                    status = Message.Status.Read,
                    notifiedAt = 0,
                    readAt = 0,
                    timestamp = 0,
                    body = Message.Text(messageText)
                )
            }
        }
    }
}

suspend operator fun <T> ConflatedBroadcastChannel<T>.invoke(f: T.() -> T?): T? =
    value.f()?.also { send(it) }
