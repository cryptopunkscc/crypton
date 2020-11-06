package cc.cryptopunks.crypton.view

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.cli.Check
import cc.cryptopunks.crypton.cli.prepare
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.core.cli.context
import cc.cryptopunks.crypton.core.cli.translateMessageInput
import cc.cryptopunks.crypton.util.ScrollHelper
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.util.logger.log
import cc.cryptopunks.crypton.widget.ActorLayout
import cc.cryptopunks.crypton.widget.autoAdjustActionButtons
import cc.cryptopunks.crypton.widget.autoAdjustPaddingOf
import cc.cryptopunks.crypton.widget.setSlashClickListener
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

class ChatView(
    context: Context,
    private val account: Address,
    private val address: Address
) :
    ActorLayout(context),
    Message.Consumer {

    var resumed: Boolean = false

    private val messageAdapter = MessageAdapter(coroutineContext)

    private val helper = ScrollHelper(context)

    private var command: Any? = null

    private var lastMessageTimestamp = 0L

    init {
        View.inflate(context, R.layout.chat, this)
        val recyclerView = chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                RecyclerView.VERTICAL,
                true
            )
            adapter = messageAdapter
        }
        messageInputView.apply {
            autoAdjustActionButtons()
            autoAdjustPaddingOf(recyclerView)
            setSlashClickListener()
        }
    }

    override fun Connector.connect(): Job = launch {
        launch {
            input.onStart {
                val rect = Rect()
                chatRecyclerView.children.filter { child ->
                    child.getGlobalVisibleRect(rect)
                }.filterIsInstance<MessageView>().mapNotNull { it.message }.let {
                    Exec.MessagesRead(it.toList()).out()
                }
            }.collect { arg ->
                when (arg) {

                    is Chat.MessageText -> messageInputView.input.setText(arg.text)

                    is Chat.PagedMessages -> {
                        messageAdapter.setMessages(arg)
                        arg.list.firstOrNull()?.takeIf {
                            it.timestamp > lastMessageTimestamp && it.status != Message.Status.State
                        }?.run {
                            lastMessageTimestamp = timestamp
                            val wasBottomReached = helper.isBottomReached(chatRecyclerView)
                            delay(50)
                            if (!wasBottomReached)
                                displayNewMessageInfo() else
                                helper.scrollToTop(chatRecyclerView)
                        }
                    }

                    is Action.Error ->
                        Exec.SaveInfoMessage(arg.message ?: arg.javaClass.name).out()

                    else -> log.d { arg }
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

    private fun displayNewMessageInfo() =
        Toast.makeText(context, "new message", Toast.LENGTH_SHORT).show()

    private fun getInputAndClear() = messageInputView.input.text.run {
        toString().also { clear() }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        chatRecyclerView.adapter = null
    }

    override fun canConsume(message: Message): Boolean =
        message.chat == address && resumed
}
