package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.module.ChatServiceModule
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatFragment : ServiceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + MessageAdapter()
        launch {
            val route = Route.Chat(arguments.toMap())
            val chatAddress = Address.from(route.chatAddress)
            val sessionCore = appCore.sessionCore()
            val chat = withContext(Dispatchers.IO) {
                sessionCore.chatRepo.get(chatAddress)
            }
            setTitle(chatAddress)
            binding + ChatServiceModule(
                chatCore = sessionCore.chatCore(chat)
            ).chatService
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChatView(context!!)
}
