package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
import cc.cryptopunks.crypton.util.FeatureManager
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import kotlin.reflect.KClass

class AppModule(
    val sys: Sys,
    val repo: Repo,
    override val mainClass: KClass<*>,
    override val createConnection: Connection.Factory,
    override val createSessionServices: (SessionCore) -> List<Session.BackgroundService>,
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor
) :
    AppCore,
    Executors,
    Sys by sys,
    Repo by repo {

    override val sessionStore = Session.Store()
    override val userPresenceStore = UserPresence.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
    override val featureManager = FeatureManager { featureCore() }
    override fun featureCore(): FeatureCore = FeatureModule(this)
}

private data class FeatureModule(
    val appCore: AppCore
) : FeatureCore,
    AppCore by appCore {

    override val routeSys: Route.Sys = appCore.routeSys

    override val navigate = Route.Navigate(routeSys)
    override fun sessionCore(): SessionCore =
        sessionCore(sessionStore.get().values.first())

    override fun sessionCore(session: Session): SessionCore =
        SessionModule(this, session)
}

private data class SessionModule(
    val featureCore: FeatureCore,
    override val session: Session
) : SessionCore,
    FeatureCore by featureCore,
    Net by session {

    override val sessionScope = Session.Scope()

    override val sessionStore = Session.Store()
    override val sessionBackgroundServices by lazy { createSessionServices(this) }
    override fun chatCore(chat: Chat): ChatCore =
        ChatModule(
            sessionCore = this,
            chat = chat
        )
}

private class ChatModule(
    sessionCore: SessionCore,
    override val chat: Chat
) : SessionCore by sessionCore,
    ChatCore
