package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.view.MessageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MessageAdapter @Inject constructor(
    override val coroutineContext: CoroutineContext
) :
    PagedListAdapter<Message, MessageAdapter.ViewHolder>(Diff),
    CoroutineScope {

    val outputChannel = BroadcastChannel<Any>(1)

    var account = Address.Empty


    private val dateFormat = SimpleDateFormat(
        "d MMM • HH:mm",
        Locale.getDefault()
    )

    private fun createView(parent: ViewGroup, viewType: Int) = MessageView(
        context = parent.context,
        type = viewType,
        dateFormat = dateFormat
    ).apply {
        launch { optionClicks.consumeEach { outputChannel.send(it) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        createView(parent, viewType)
    )

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
        )= oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Message,
            newItem: Message
        ) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val view get() = itemView as MessageView
    }
}