package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Chat.Service.*
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatView(
    context: Context
) :
    ServiceLayout(context) {

    private val log = typedLog()

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
        }
    }

    override fun Service.Connector.connect(): Job = launch {
        launch {
            input.collect { arg ->
                log.d("in: $arg")
                when (arg) {
                    is RecyclerView.Adapter<*> -> {
                        log.d("set adapter: $arg")
                        chatRecyclerView.adapter = arg
                    }
                    is Service.Actor.Start -> chatRecyclerView.run {
                        val rect = Rect()
                        children.filter { child ->
                            getHitRect(rect)
                            child.getLocalVisibleRect(rect)
                        }.filterIsInstance<MessageView>().mapNotNull { it.message }.let {
                            MessagesRead(it.toList())
                        }
                    }
                    is MessageText -> messageInputView.input.setText(arg.text)
                    is Messages -> {
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
            messageInputView.button.clicks().map { SendMessage(getInputAndClear()) }
                .collect(output)
        }
        launch {
            Service.Actor.Connected.out()
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
}