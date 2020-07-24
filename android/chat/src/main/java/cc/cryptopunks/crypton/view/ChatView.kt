package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.cli.context
import cc.cryptopunks.crypton.cli.translateMessageInput
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.translator.Check
import cc.cryptopunks.crypton.translator.prepare
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.chat.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.math.abs

class ChatView(
    context: Context,
    private val account: Address,
    private val address: Address
) :
    ActorLayout(context),
    Message.Consumer {

    private val log = typedLog()

    private val messageAdapter = MessageAdapter(coroutineContext)

    private val scrollThreshold: Int = context.resources.displayMetrics.run {
        scaledDensity * SCROLL_THRESHOLD_DP
    }.toInt()

    private val orientationThreshold = context.resources.displayMetrics.density * 48 * 2

    private var command: Any? = null

    private var lastMessageTimestamp = 0L

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
        messageInputView.apply {
            slash.setOnClickListener {
                input.apply {
                    selectionEnd.let { selection ->
                        if (text.firstOrNull() == '/') {
                            setText(text.drop(1))
                            setSelection(selection - 1)
                        } else {
                            setText("/$text")
                            setSelection(selection + 1)
                        }
                    }
                }
            }
        }
        messageInputView.addOnLayoutChangeListener { view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            chatRecyclerView.apply {
                setPadding(paddingLeft, paddingTop, paddingRight, this.bottom - top)
            }

            if (top != oldTop) messageInputView.actionWrapper.orientation =
                when (abs(top - bottom) > orientationThreshold) {
                    true -> LinearLayout.VERTICAL
                    false -> LinearLayout.HORIZONTAL
                }
        }
    }

    override fun Connector.connect(): Job = launch {
        launch {
            input.onStart {
                chatRecyclerView.run {
                    val rect = Rect()
                    children.filter { child ->
                        getHitRect(rect)
                        child.getLocalVisibleRect(rect)
                    }.filterIsInstance<MessageView>().mapNotNull { it.message }.let {
                        Exec.MessagesRead(it.toList()).out()
                    }
                }
            }.collect { arg ->
                when (arg) {

                    is Chat.Service.MessageText -> messageInputView.input.setText(arg.text)

                    is Chat.Service.PagedMessages -> {
                        messageAdapter.setMessages(arg)
                        arg.list.firstOrNull()?.takeIf {
                            it.timestamp > lastMessageTimestamp && it.status != Message.Status.State
                        }?.run {
                            lastMessageTimestamp = timestamp
                            val wasBottomReached = isBottomReached()
                            delay(50)
                            if (!wasBottomReached)
                                displayNewMessageInfo() else
                                scrollToNewMessage()
                        }
                    }

                    is Action.Error ->
                        Exec.SaveInfoMessage(arg.message ?: arg.javaClass.name).out()

                    else -> log.d(arg)
                }
            }
        }
        launch {
            flowOf(
                messageAdapter.outputFlow(),
                messageInputView.button.clicks().mapNotNull {
                    when (command) {
                        is Check.Suggest ->
                            Toast.makeText(
                                context,
                                command.toString(),
                                Toast.LENGTH_SHORT
                            ).run {
                                setGravity(
                                    Gravity.TOP or Gravity.CENTER_HORIZONTAL,
                                    0, IntArray(2).also {
                                        messageInputView.getLocationInWindow(it)
                                    }[1] - messageInputView.measuredHeight
                                )
                                show()
                                null
                            }

                        else -> {
                            if (command != null) getInputAndClear()
                            (command as? Exec.EnqueueMessage)
                                ?.copy(encrypted = messageInputView.encrypt.isChecked)
                                ?: command
                        }
                    }
                }
            ).flattenMerge()
                .collect(output)
        }
        launch {
            messageInputView.input.textChanges().debounce(100).translateMessageInput(
                context(
                    route = Route.Chat(
                        account = account,
                        address = address
                    )
                ).prepare()
            ).collect {
                command = it
            }
        }
        launch {
            delay(5)
            Get.PagedMessages.out()
            Subscribe.PagedMessages(true).out()
        }
    }.apply {
        invokeOnCompletion {
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

    override fun canConsume(message: Message): Boolean = message.chat == address
}
