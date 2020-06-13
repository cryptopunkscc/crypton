package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import kotlin.reflect.KClass

interface AppCore :
    Executors,
    Sys,
    Repo {

    val mainClass: KClass<*>
    val navigate: Route.Navigate

    val sessionStore: Session.Store
    val userPresenceStore: Presence.Store
    val clipboardStore: Clip.Board.Store
    val connectableBindingsStore: Connectable.Binding.Store

    val createSessionServices: (SessionCore) -> List<Session.BackgroundService>
    val createConnection: Connection.Factory

    fun sessionCore(): SessionCore
    fun sessionCore(address: Address): SessionCore
    fun sessionCore(session: Session): SessionCore
}

interface SessionCore :
    AppCore,
    SessionRepo,
    Net {

    val session: Session
    val sessionScope: Session.Scope
    val sessionBackgroundServices: List<Session.BackgroundService>
    fun chatCore(chat: Chat): ChatCore
    suspend fun chatCore(chat: Address): ChatCore
}

interface ChatCore :
    SessionCore {

    val chat: Chat
}
