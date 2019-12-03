package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ChatFragment : PresenterFragment<ChatPresenter.Actor, ChatPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { presentation.setPresenter(presenter()) }
    }

    private suspend fun presenter(): ChatPresenter = let {
        val route = Route.Chat(arguments.toMap())
        val chat = runBlocking(Dispatchers.IO) {
            featureCore.chatRepo.get(Address.from(route.chatAddress))
        }
        setTitle(chat.address)
        featureCore
            .sessionFeature()
            .chatFeature(chat)
            .resolve<ChatPresenter.Core>()
            .chatPresenter
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