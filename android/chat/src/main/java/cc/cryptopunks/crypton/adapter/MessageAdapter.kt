package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.ChatService.*
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.view.MessageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MessageAdapter(
    override val coroutineContext: CoroutineContext
) :
    PagedListAdapter<Message, MessageAdapter.ViewHolder>(Diff),
    CoroutineScope,
    Service.Connectable {

    private val log = typedLog()

    val clicksChannel = BroadcastChannel<MessageOption>(Channel.BUFFERED)

    val readChannel = BroadcastChannel<Message>(Channel.BUFFERED)

    private val channel = BroadcastChannel<Message>(Channel.BUFFERED)

    var account = Address.Empty

    var stop = false

    private var state: Any = Stop

    private val dateFormat = SimpleDateFormat(
        "d MMM • HH:mm",
        Locale.getDefault()
    )

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        }
    }

    override fun Service.Connector.connect(): Job = launch {
        launch {
            input.collect {
                when (it) {
                    is Start -> {
                        state = it
                        this@MessageAdapter.out()
                    }
                    is Stop -> {
                        state = it
                    }
                    is Messages -> {
                        account = it.account
                        submitList(it.list)
                    }
                }
            }
        }
        launch {
            this@MessageAdapter.out()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(onScrollListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerView.removeOnScrollListener(onScrollListener)
    }

    private fun createView(parent: ViewGroup, viewType: Int) = MessageView(
        context = parent.context,
        type = viewType,
        dateFormat = dateFormat
    ).apply {
        launch { optionClicks.consumeEach { clicksChannel.send(it) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        createView(parent, viewType)
    )

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.view.message?.let { message ->
//            if (state != Stop && message.isUnread) {
            if (!stop && message.isUnread) {
                launch {
                    channel.send(message)
                    readChannel.send(message)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.message = getItem(position)
    }

    override fun getItemViewType(position: Int): Int =
        if ((getItem(position))?.from?.address == account)
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view get() = itemView as MessageView
    }
}