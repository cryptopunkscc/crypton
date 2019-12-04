package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.presenter.ChatService.Input.SendMessage
import cc.cryptopunks.crypton.presenter.ChatService.Output.MessageText
import cc.cryptopunks.crypton.presenter.ChatService.Output.Messages
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatView(
    context: Context
) :
    FrameLayout(context),
    Service.Wrapper {

    private val scope = Actor.Scope()

    override val wrapper = wrapper(scope)

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

    override fun onInvoke() {
        scope.launch {
            messageInputView.button.clicks().collect {
                SendMessage(getInputAndClear()).out()
            }
        }
    }

    override suspend fun Any.onInput() {
        when (this) {
            is MessageText -> messageInputView.input.setText(text)
            is Messages -> isBottomReached()
                .also { messageAdapter.submitList(list) }
                .let { wasBottomReached ->
                    if (wasBottomReached)
                        scrollToNewMessage() else
                        displayNewMessageInfo()
                }
        }
    }

    private fun isBottomReached() = chatRecyclerView.run {
        val maxScroll = computeVerticalScrollRange()
        val currentScroll = computeVerticalScrollOffset() + computeVerticalScrollExtent()
        maxScroll - currentScroll < scrollThreshold
    }

    private fun scrollToNewMessage() =
        chatRecyclerView.smoothScrollToPosition(0)

    private fun displayNewMessageInfo() =
        Toast.makeText(context, "new message", Toast.LENGTH_SHORT).show()

    private fun getInputAndClear() = messageInputView.input.text.run {
        toString().also { clear() }
    }

    private companion object {
        const val SCROLL_THRESHOLD_DP = 100
    }
}