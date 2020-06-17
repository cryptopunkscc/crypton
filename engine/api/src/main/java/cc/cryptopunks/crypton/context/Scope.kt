package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.TypedLog
import kotlinx.coroutines.CoroutineScope
import kotlin.reflect.KClass

interface AppScope :
    CoroutineScope,
    Executors,
    Sys,
    Repo {

    val log: TypedLog
    val mainClass: KClass<*>
    val navigate: Route.Navigate

    val sessionStore: Session.Store
    val presenceStore: Presence.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store

    val createSessionServices: (SessionScope) -> List<Session.BackgroundService>
    val createConnection: Connection.Factory

    fun sessionScope(): SessionScope
    fun sessionScope(address: Address): SessionScope
    fun sessionScope(session: Session): SessionScope
}

interface SessionScope :
    AppScope,
    SessionRepo,
    Net {

    val session: Session
    val sessionScope: Session.Scope
    val sessionBackgroundServices: List<Session.BackgroundService>
    fun chatScope(chat: Chat): ChatScope
    suspend fun chatScope(chat: Address): ChatScope
}

interface ChatScope :
    SessionScope {

    val chat: Chat
}
