package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.navigation.Route
import cc.cryptopunks.crypton.presentation.PresentationComponent
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.presenter.DaggerChatPresenter_Component
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
            component.chatRepo.get(Address.from(route.chatAddress))
        }
        DaggerChatPresenter_Component.builder()
            .module(ChatPresenter.Module(chat))
            .executorsComponent(component)
            .component(navigationComponent)
            .address(address)
            .repo(messageRepo)
            .repo(chatRepo)
            .api(component as Message.Api)
            .api(component as Chat.Api)
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