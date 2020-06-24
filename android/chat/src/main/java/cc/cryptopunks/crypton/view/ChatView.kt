package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat.Service.MessageText
import cc.cryptopunks.crypton.context.Chat.Service.MessagesRead
import cc.cryptopunks.crypton.context.Chat.Service.PagedMessages
import cc.cryptopunks.crypton.context.Chat.Service.EnqueueMessage
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatView(
    context: Context,
    private val address: Address
) :
    ActorLayout(context),
    Message.Consumer {

    private val messageAdapter = MessageAdapter(coroutineContext)

    private val scrollThreshold: Int = context.resources.displayMetrics.run {
        scaledDensity * SCROLL_THRESHOLD_DP
    }.toInt()

    init {
        View.inflate(context, R.layout.chat, this)
        chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                true
            )
            adapter = messageAdapter
        }
    }

    override fun Connector.connect(): Job = launch {
        launch {
            input.collect { arg ->
                when (arg) {
                    is Actor.Start -> chatRecyclerView.run {
                        val rect = Rect()
                        children.filter { child ->
                            getHitRect(rect)
                            child.getLocalVisibleRect(rect)
                        }.filterIsInstance<MessageView>().mapNotNull { it.message }.let {
                            MessagesRead(it.toList())
                        }
                    }
                    is MessageText -> messageInputView.input.setText(arg.text)
                    is PagedMessages -> {
                        messageAdapter.setMessages(arg)
                        val wasBottomReached = isBottomReached()
                        delay(50)
                        if (wasBottomReached)
                            scrollToNewMessage() else
                            displayNewMessageInfo()
                    }
                }
            }
        }
        launch {
            flowOf(
                messageAdapter.outputFlow(),
                messageInputView.button.clicks().map { EnqueueMessage(getInputAndClear()) }
            ).flattenMerge()
                .collect(output)
        }
        launch {
            Actor.Connected.out()
        }
        invokeOnClose {
            messageAdapter.setMessages(null)
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        chatRecyclerView.adapter = null
    }

    private companion object {
        const val SCROLL_THRESHOLD_DP = 100
    }

    override fun canConsume(message: Message): Boolean = message.chatAddress == address
}
