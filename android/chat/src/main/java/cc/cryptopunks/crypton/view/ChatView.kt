package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ChatView(
    context: Context,
    scope: Actor.Scope
) :
    FrameLayout(context),
    ChatPresenter.Actor {

    private val messageAdapter: MessageAdapter = MessageAdapter(scope)
    private val scrollThreshold: Int =
        (context.resources.displayMetrics.scaledDensity * SCROLL_THRESHOLD_DP).toInt()

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

    override val setInputMessage: (CharSequence?) -> Unit
        get() = { text ->
            messageInputView.input.setText(text)
        }

    override val setMessages: suspend (PagedList<MessagePresenter>) -> Unit
        get() = { list ->
            isBottomReached
                .also {
                    messageAdapter.submitList(list)
                }
                .let { wasBottomReached ->
                    if (wasBottomReached)
                        scrollToNewMessage() else
                        displayNewMessageInfo()
                }
        }

    private val isBottomReached
        get() = chatRecyclerView.run {
            val maxScroll = computeVerticalScrollRange()
            val currentScroll = computeVerticalScrollOffset() + computeVerticalScrollExtent()
            maxScroll - currentScroll < scrollThreshold
        }


    private fun scrollToNewMessage() =
        chatRecyclerView.smoothScrollToPosition(0)

    private fun displayNewMessageInfo() =
        Toast.makeText(context, "new message", Toast.LENGTH_SHORT).show()


    override val sendMessageFlow: Flow<String> = messageInputView.button
        .clicks()
        .map { getInputAndClear() }

    private fun getInputAndClear() = messageInputView.input.text.run {
        toString().also { clear() }
    }

    private companion object {
        const val SCROLL_THRESHOLD_DP = 100
    }
}