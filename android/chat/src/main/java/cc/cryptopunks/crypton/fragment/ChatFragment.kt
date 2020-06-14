package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.service.ChatService
import cc.cryptopunks.crypton.util.toMap
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    private val route get() = Route.Chat(arguments.toMap())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + MessageAdapter()
        launch {
            val route = route
            val accountAddress = route.accountAddress
            val chatAddress = route.address
            setTitle(chatAddress)
            binding + ChatService(
                chatCore = appCore
                    .sessionCore(accountAddress)
                    .chatCore(chatAddress)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChatView(context!!, route.address)
}
