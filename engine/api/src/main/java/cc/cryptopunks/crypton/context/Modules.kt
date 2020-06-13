package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.Executors
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
    override val userPresenceStore = Presence.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
    override val navigate = Route.Navigate(routeSys)
    override fun sessionCore(): SessionCore = sessionCore(sessionStore.get().values.first())
    override fun sessionCore(address: Address): SessionCore = sessionCore(sessionStore.get()[address]!!)
    override fun sessionCore(session: Session): SessionCore = SessionModule(this, session)
}

private data class SessionModule(
    val appCore: AppCore,
    override val session: Session
) :
    SessionCore,
    AppCore by appCore,
    Net by session,
    SessionRepo by session {

    override val sessionScope = Session.Scope()
    override val sessionBackgroundServices by lazy { createSessionServices(this) }
    override fun chatCore(chat: Chat): ChatCore = ChatModule(this, chat)
    override suspend fun chatCore(chatAddress: Address): ChatCore = chatCore(chatRepo.get(chatAddress))
}

private class ChatModule(
    sessionCore: SessionCore,
    override val chat: Chat
) :
    SessionCore by sessionCore,
    ChatCore
