package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.isFrom
import cc.cryptopunks.crypton.context.isUnread
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.logger.typedLog
import cc.cryptopunks.crypton.view.MessageView
import cc.cryptopunks.crypton.view.ResolveUrlBody
import cc.cryptopunks.crypton.widget.GenericViewHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

private typealias ViewHolder = GenericViewHolder<MessageView>

class MessageAdapter(
    override val coroutineContext: CoroutineContext,
    val resolveUrlBody: ResolveUrlBody,
) :
    PagedListAdapter<Message, ViewHolder>(Diff),
    CoroutineScope {

    private val log = typedLog()

    private val clicks = BroadcastChannel<Any>(Channel.BUFFERED)

    private val read = BroadcastChannel<Message>(Channel.BUFFERED)

    private var account = Address.Empty

    private val dateFormat = SimpleDateFormat("d MMM • HH:mm", Locale.getDefault())

    fun setMessages(messages: Chat.PagedMessages?) {
        log.d { "submit messages $messages" }
        account = messages?.account ?: account
        submitList(messages?.list)
    }

    fun outputFlow() = flowOf(
        clicks.asFlow(),
        read.asFlow().bufferedThrottle(200).map { Exec.MessagesRead(it) }
    ).flattenMerge()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MessageView(
            context = parent.context,
            coroutineContext = coroutineContext,
            type = viewType,
            dateFormat = dateFormat,
            resolveUrlBody = resolveUrlBody
        )
    )

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.view.message?.let { message ->
            if (message.isUnread) launch {
                read.send(message)
            }
        }
        holder.view.apply {
            job = launch { optionClicks.consumeEach { clicks.send(it) } }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.message = getItem(position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.view.job?.cancel()
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)?.isFrom(account)) {
            null -> Gravity.CENTER_HORIZONTAL
            true -> Gravity.RIGHT
            false -> Gravity.LEFT
        }

    private object Diff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message,
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message,
        ) = oldItem == newItem
    }
}
