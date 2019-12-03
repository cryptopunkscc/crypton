package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

interface Net :
    Account.Net,
    User.Net,
    Presence.Net,
    Message.Net,
    Chat.Net,
    RosterEvent.Net,
    UserPresence.Net {

    val connect: Connect
    val disconnect: Disconnect
    val interrupt: Interrupt
    val isConnected: IsConnected
    val initOmemo: InitOmemo
    val netEvents: Event.Output

    interface Connect : () -> Unit
    interface Disconnect : () -> Unit
    interface Interrupt : () -> Unit
    interface IsConnected : () -> Boolean
    interface InitOmemo : () -> Boolean

    interface Event : Api.Event {

        interface Output : Flow<Api.Event>

        object Connected : Event

        object OmemoInitialized : Event

        data class Disconnected(
            val throwable: Throwable? = null
        ) : Event {
            val hasError get() = throwable != null
        }
    }

    open class Exception(
        message: String? = null,
        cause: Throwable? = null
    ) :
        kotlin.Exception(message, cause),
        Event {

        interface Output : Flow<Exception>
    }
}