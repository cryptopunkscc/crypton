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
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + MessageAdapter()
        launch {
            val route = Route.Chat(arguments.toMap())
            val accountAddress = Address.from(route.accountId)
            val chatAddress = Address.from(route.chatAddress)
            setTitle(chatAddress)
            binding + ChatServiceModule(
                chatCore = appCore
                    .sessionCore(accountAddress)
                    .chatCore(chatAddress)
            ).chatService
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChatView(context!!)
}
