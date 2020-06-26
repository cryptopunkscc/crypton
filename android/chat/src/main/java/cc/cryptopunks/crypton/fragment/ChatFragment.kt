package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.navigate.account
import cc.cryptopunks.crypton.navigate.chat
import cc.cryptopunks.crypton.service.chatService
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding + viewProxy
        binding + MessageAdapter()
        launch {
            val args = requireArguments()
            val accountAddress = args.account!!
            val chatAddress = args.chat!!
            setTitle(chatAddress)
            binding + chatService(
                scope = appScope
                    .sessionScope(accountAddress)
                    .chatScope(chatAddress)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ChatView(requireContext(), requireArguments().chat!!)
}
