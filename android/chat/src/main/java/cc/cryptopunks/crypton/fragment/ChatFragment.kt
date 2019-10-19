package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ChatFragment : PresenterFragment<
        ChatPresenter.View,
        ChatPresenter,
        ChatFragment.Component>() {

    @dagger.Component(
        dependencies = [PresentationComponent::class],
        modules = [Module::class]
    )
    interface Component : Presenter.Component<ChatPresenter>

    @dagger.Module
    data class Module(@get:Provides val chat: Chat)

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ): Component = let {
        val route = Route.Chat(arguments.toMap())
        val chat = runBlocking(Dispatchers.IO) {
            component.chatRepo.get(Address.from(route.chatAddress))
        }
        DaggerChatFragment_Component
            .builder()
            .presentationComponent(presentationComponent)
            .module(Module(chat))
            .build()
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