package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.chatScope
import cc.cryptopunks.crypton.context.sessionScope
import cc.cryptopunks.crypton.feature.ShowFileChooser
import cc.cryptopunks.crypton.navigate.account
import cc.cryptopunks.crypton.navigate.chat
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.launch

class ChatFragment : ServiceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateService(): Connectable? =
        requireArguments().run {
            launch {
                binding + rootScope
                    .sessionScope(account!!)
                    .chatScope(chat!!)
                    .fragmentScope()
                    .service(serviceName)
            }
            null
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = requireArguments().run {
        setTitle(chat!!)
        ChatView(
            context = requireContext(),
            rootScope = rootScope,
            account = account!!,
            address = chat!!
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.attach_file -> ShowFileChooser
        else -> null
    }?.let(binding::send) != null

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
