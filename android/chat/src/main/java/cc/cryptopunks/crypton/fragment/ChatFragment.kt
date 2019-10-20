package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.coreComponent
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.presenter.DaggerChatPresenter_Component
import cc.cryptopunks.crypton.repo.repo
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class ChatFragment : PresenterFragment<
        ChatPresenter.View,
        ChatPresenter,
        ChatPresenter.Component>() {

    override suspend fun onCreateComponent(
        component: PresentationComponent
    ) = component.run {
        val route = Route.Chat(arguments.toMap())
        val chat = runBlocking(Dispatchers.IO) {
            coreComponent.repo<Chat.Repo>().get(Address.from(route.chatAddress))
        }
        DaggerChatPresenter_Component.builder()
            .module(ChatPresenter.Module(chat))
            .scope(apiScope)
            .executorsComponent(coreComponent)
            .component(navigationComponent)
            .address(address)
            .repo(coreComponent.repo<Message.Repo>())
            .repo(coreComponent.repo<Chat.Repo>())
            .net(net<Message.Net>())
            .net(net<Chat.Net>())
            .build()!!
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