package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ChatService.*
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChatView(
    context: Context
) :
    ServiceLayout(context) {

    private val messageAdapter = MessageAdapter(coroutineContext)

    private val scrollThreshold: Int = context.resources.displayMetrics.run {
        scaledDensity * SCROLL_THRESHOLD_DP
    }.toInt()

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

    override fun Service.Connector.connect(): Job = launch {
        launch {
            input.collect { arg ->
                when (arg) {
                    is MessageText -> messageInputView.input.setText(arg.text)
                    is Messages -> {
                        val wasBottomReached = isBottomReached()
                        messageAdapter.apply {
                            account = arg.account
                            submitList(arg.list)
                        }
                        if (wasBottomReached)
                            scrollToNewMessage() else
                            displayNewMessageInfo()
                    }
                }
            }
        }
        launch {
            flowOf(
                messageAdapter.outputChannel.asFlow(),
                messageInputView.button.clicks().map { SendMessage(getInputAndClear()) }
            )
                .flattenMerge()
                .collect(output)

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