package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.dep
import kotlinx.coroutines.flow.Flow

val SessionScope.net: Net by dep()

interface Net {

    fun connect()
    fun disconnect()
    fun interrupt()
    fun isConnected(): Boolean
    suspend fun initOmemo()
    fun isOmemoInitialized(): Boolean
    fun netEvents(): Flow<Api.Event>

    interface Event : Api.Event, Scoped<SessionScope>, Async

    object Connected : Event
    object OmemoInitialized : Event
    data class Disconnected(val throwable: Throwable? = null) : Event

    open class Exception(
        message: String? = null,
        cause: Throwable? = null,
    ) :
        kotlin.Exception(message, cause),
        Event
}
