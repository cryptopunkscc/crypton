package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.ChatPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatView(
    context: Context,
    scope: Actor.Scope
) :
    FrameLayout(context),
    ChatPresenter.View,
    CoroutineScope by scope {

    private val messageAdapter = MessageAdapter(scope)

    init {
        View.inflate(context, R.layout.chat, this)
        chatRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                true
            )
        }
    }

    override val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
        get() = {
            messageAdapter.submitList(it)
        }

    override val sendMessageFlow: Flow<String> = sendMessageButton
        .clicks()
        .map { messageInput.text.toString() }
}