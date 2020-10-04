package cc.cryptopunks.crypton.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.intent.processActivityResult
import cc.cryptopunks.crypton.navigate.account
import cc.cryptopunks.crypton.navigate.chat
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    override fun onCreateService(): Connectable? =
        launch {
            val args = requireArguments()
            val accountAddress = args.account!!
            val chatAddress = args.chat!!
            setTitle(chatAddress)
            binding + rootScope
                .sessionScope(accountAddress)
                .chatScope(chatAddress)
                .fragmentScope()
                .service(serviceName)
        }.let { null }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = requireArguments().run {
        ChatView(
            context = requireContext(),
            this@ChatFragment,
            rootScope = rootScope,
            account = account!!,
            address = chat!!
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) = binding.processActivityResult(
        requestCode = requestCode,
        resultCode = resultCode,
        intent = data
    )

    override fun onResume() {
        super.onResume()
        (view as? ChatView)?.resumed = true
    }

    override fun onPause() {
        (view as? ChatView)?.resumed = false
        super.onPause()
    }

    override fun onDestroy() {
        binding.send(Exec.ClearInfoMessages)
        super.onDestroy()
    }
}
