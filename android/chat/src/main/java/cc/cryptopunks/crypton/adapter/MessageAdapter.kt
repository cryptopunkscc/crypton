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
import cc.cryptopunks.crypton.util.Store
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.ext.map
import cc.cryptopunks.crypton.util.typedLog
import cc.cryptopunks.crypton.view.MessageView
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MessageAdapter(
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main
) :
    PagedListAdapter<Message, MessageAdapter.ViewHolder>(Diff),
    CoroutineScope,
    Service.Connectable {

    private val log = typedLog()

    private val clicksChannel = BroadcastChannel<MessageOption>(Channel.BUFFERED)

    private val readChannel = BroadcastChannel<Message>(Channel.BUFFERED)

    private var account = Address.Empty

    private val store = Store<Any>(Service.Actor.Stop)

    private val dateFormat = SimpleDateFormat("d MMM • HH:mm", Locale.getDefault())

    override fun Service.Connector.connect(): Job = launch {
        launch {
            input.collect {
                log.d("in: $it")
                when (it) {
                    is Service.Actor.Start -> {
                        store { it }
                    }
                    is Service.Actor.Stop -> {
                        store { it }
                    }
                    is Messages -> {
                        log.d("submit messages $it")
                        account = it.account
                        submitList(it.list)
                    }
                    is Service.Actor.Connected -> {
                        this@MessageAdapter.out()
                    }
                }
            }
        }
        launch {
            flowOf(
                clicksChannel.asFlow(),
                readChannel.asFlow().bufferedThrottle(200).map { MessagesRead(it) }
            )
                .flattenMerge()
                .collect(output)
        }
        invokeOnClose {
            submitList(null)
        }
    }

    private fun createView(parent: ViewGroup, viewType: Int) = MessageView(
        context = parent.context,
        type = viewType,
        dateFormat = dateFormat
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        createView(parent, viewType)
    )

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.view.message?.let { message ->
            if (store.get() != Service.Actor.Stop && message.isUnread) launch {
                readChannel.send(message)
            }
        }
        holder.view.apply {
            job = launch { optionClicks.consumeEach { clicksChannel.send(it) } }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.message = getItem(position)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.view.apply {
            job?.cancel()
        }
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