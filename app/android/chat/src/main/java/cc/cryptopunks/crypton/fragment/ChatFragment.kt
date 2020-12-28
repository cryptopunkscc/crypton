package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.connectService
import cc.cryptopunks.crypton.connector
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.createChatScope
import cc.cryptopunks.crypton.context.getSessionScope
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.feature.ShowFileChooser
import cc.cryptopunks.crypton.navigate.account
import cc.cryptopunks.crypton.navigate.chat
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import cc.cryptopunks.crypton.view.ChatView
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.ContinuationInterceptor

class ChatFragment :
    ServiceFragment(),
    SessionScope {

    override val coroutineContext by lazy {
        cryptonContext(
            super.coroutineContext,
            rootScope.getSessionScope(requireArguments().account!!).coroutineContext
                .minusKey(Job)
                .minusKey(ContinuationInterceptor),
            asDep<Fragment>()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        launch {
            connector().connectService()
            binding + createChatScope(this@ChatFragment, requireArguments().chat!!)
                .service(serviceName)
        }
    }

    override fun onCreateService(): Connectable? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
