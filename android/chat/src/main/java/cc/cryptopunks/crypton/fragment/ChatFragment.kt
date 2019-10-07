package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.component.DaggerChatFragmentComponent
import cc.cryptopunks.crypton.feature.chat.presenter.ChatPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.MessagePresenter
import cc.cryptopunks.crypton.module.FragmentModule
import cc.cryptopunks.crypton.util.invoke
import cc.cryptopunks.crypton.util.reactivebindings.flowClicks
import kotlinx.android.synthetic.main.chat.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatFragment : CoreFragment() {

    override val layoutRes: Int get() = R.layout.chat

    val component = async(
        start = CoroutineStart.LAZY,
        context = Dispatchers.IO
    ) {
        DaggerChatFragmentComponent.builder()
            .coreComponent(app.component)
            .fragmentComponent(FragmentModule(this@ChatFragment))
            .build()
            .createChatComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch { component.await() }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        launch { component.await().presentChat(View()) }
    }

    inner class View : ChatPresenter.View {

        private val messageAdapter = MessageAdapter(scope)

        init {
            chatRecyclerView.apply {
                adapter = messageAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }

        override val setMessages: suspend (PagedList<MessagePresenter>) -> Unit = {
            messageAdapter.submitList(it)
        }

        override val sendMessageFlow: Flow<String> = sendMessageButton
            .flowClicks()
            .map { messageInput.text.toString() }
    }
}