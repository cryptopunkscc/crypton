package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.view.MessageView
import cc.cryptopunks.crypton.widget.GenericViewHolder
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

private typealias ViewHolder = GenericViewHolder<MessageView>

class MessageAdapter(
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main
) :
    PagedListAdapter<Message, ViewHolder>(Diff),
    CoroutineScope,
    Connectable {

    private val log = typedLog()

    private val clicks = BroadcastChannel<Any>(Channel.BUFFERED)

    private val read = BroadcastChannel<Message>(Channel.BUFFERED)

    private var account = Address.Empty

    private var actorStatus: Any = Actor.Stop

    private val dateFormat = SimpleDateFormat("d MMM • HH:mm", Locale.getDefault())

    fun setMessages(messages: Chat.Service.PagedMessages?) {
        log.d("submit messages $messages")
        account = messages?.account ?: account
        submitList(messages?.list)
    }

    fun outputFlow() = flowOf(
        clicks.asFlow(),
        read.asFlow().bufferedThrottle(200).map { Chat.Service.MessagesRead(it) }
    ).flattenMerge()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        MessageView(
            context = parent.context,
            type = viewType,
            dateFormat = dateFormat
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
        if (getItem(position)?.from?.address == account)
            Gravity.RIGHT else
            Gravity.LEFT

    private object Diff : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(
            oldItem: Message,
            newItem: Message
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ) = oldItem == newItem
    }
}
