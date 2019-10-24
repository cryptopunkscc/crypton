package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.androidCore
import cc.cryptopunks.crypton.core
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatFragment : PresenterFragment<ChatPresenter.View, ChatPresenter>() {

    @dagger.Component(
        dependencies = [
            Core::class,
            Navigation::class,
            Session::class
        ],
        modules = [Module::class]
    )
    interface Component : Presenter.Component<ChatPresenter>

    @dagger.Module
    data class Module(@get:Provides val chat: Chat)

    private suspend fun presenter(): ChatPresenter = androidCore.run {
        val route = Route.Chat(arguments.toMap())
        val chat = runBlocking(Dispatchers.IO) {
            chatRepo.get(Address.from(route.chatAddress))
        }
        DaggerChatFragment_Component.builder()
            .core(core)
            .navigation(navigation)
            .session(currentSession())
            .module(Module(chat))
            .build()
            .presenter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { presentation.setPresenter(presenter()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChatView(
        context = context!!,
        scope = presentation.actorScope
    )
}