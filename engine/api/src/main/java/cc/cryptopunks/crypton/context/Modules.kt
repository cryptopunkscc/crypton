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
    override val createSessionServices: (SessionScope) -> List<Session.BackgroundService>,
    override val mainExecutor: MainExecutor,
    override val ioExecutor: IOExecutor
) :
    AppScope,
    Executors,
    Sys by sys,
    Repo by repo {

    override val sessionStore = Session.Store()
    override val presenceStore = Presence.Store()
    override val clipboardStore = Clip.Board.Store()
    override val connectableBindingsStore = Connectable.Binding.Store()
    override val navigate = Route.Navigate(routeSys)
    override fun sessionScope(): SessionScope = sessionScope(sessionStore.get().values.first())
    override fun sessionScope(address: Address): SessionScope = sessionScope(sessionStore.get()[address]!!)
    override fun sessionScope(session: Session): SessionScope = SessionModule(this, session)
}

private data class SessionModule(
    val appScope: AppScope,
    override val session: Session
) :
    SessionScope,
    AppScope by appScope,
    Net by session,
    SessionRepo by session {

    override val sessionScope = Session.Scope()
    override val sessionBackgroundServices by lazy { createSessionServices(this) }
    override fun chatScope(chat: Chat): ChatScope = ChatModule(this, chat)
    override suspend fun chatScope(chatAddress: Address): ChatScope = chatScope(chatRepo.get(chatAddress))
}

private class ChatModule(
    sessionScope: SessionScope,
    override val chat: Chat
) :
    SessionScope by sessionScope,
    ChatScope
