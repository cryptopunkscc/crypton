package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.FeatureManager
import kotlin.reflect.KClass

interface AppCore :
    Executors,
    Sys,
    Repo {

    val mainClass: KClass<*>
    val sessionStore: Session.Store
    val userPresenceStore: UserPresence.Store
    val createSessionServices: (SessionCore) -> List<Session.BackgroundService>
    val clipboardStore: Clip.Board.Store
    val createConnection: Connection.Factory
    val connectableBindingsStore: Connectable.Binding.Store
    val featureManager: FeatureManager
    fun featureCore(): FeatureCore
}

interface FeatureCore :
    AppCore {

    val navigate: Route.Navigate
    fun sessionCore(): SessionCore
    fun sessionCore(session: Session): SessionCore
}

interface SessionCore :
    FeatureCore,
    Net {

    val session: Session
    val sessionScope: Session.Scope
    val sessionBackgroundServices: List<Session.BackgroundService>
    fun chatCore(chat: Chat): ChatCore
}

interface ChatCore :
    SessionCore {

    val chat: Chat
}
