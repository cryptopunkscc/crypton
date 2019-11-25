package cc.cryptopunks.crypton.net

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.*
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
    val isConnected: IsConnected
    val initOmemo: InitOmemo
    val netEvents: Event.Output

    interface Connect : () -> Unit
    interface Disconnect : () -> Unit
    interface IsConnected : () -> Boolean
    interface InitOmemo : () -> Unit

    interface Event : Api.Event {

        interface Output : Flow<Api.Event>

        object Connected : Event

        data class Disconnected(
            val throwable: Throwable? = null
        ) : Event {
            val withError get() = throwable != null
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