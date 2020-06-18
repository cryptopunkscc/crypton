package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow

interface Net :
    Account.Net,
    User.Net,
    Presence.Net,
    Message.Net,
    Chat.Net,
    Roster.Net {

    fun connect()
    fun disconnect()
    fun interrupt()
    fun isConnected(): Boolean
    fun initOmemo(): Boolean
    fun netEvents(): Flow<Api.Event>

    interface Event : Api.Event

    object Connected : Event
    object OmemoInitialized : Event
    data class Disconnected(
        val throwable: Throwable? = null
    ) : Event {
        val hasError get() = throwable != null
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
