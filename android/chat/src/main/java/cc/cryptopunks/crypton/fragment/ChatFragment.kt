package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.navigate.account
import cc.cryptopunks.crypton.navigate.chat
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    override fun onCreateService(): Connectable? {
        launch {
            val args = requireArguments()
            val accountAddress = args.account!!
            val chatAddress = args.chat!!
            setTitle(chatAddress)
            binding + appScope
                .sessionScope(accountAddress)
                .chatScope(chatAddress)
        }
        return MessageAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = requireArguments().run {
        ChatView(requireContext(), account!!, chat!!)
    }

    override fun onDestroy() {
        binding.send(Chat.Service.ClearInfoMessages)
        super.onDestroy()
    }
}
