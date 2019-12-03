package cc.cryptopunks.crypton.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.presenter.MessagePresenter
import cc.cryptopunks.crypton.view.MessageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MessageAdapter @Inject constructor(
    private val scope: CoroutineScope
) :
    PagedListAdapter<MessagePresenter, MessageAdapter.ViewHolder>(Diff) {

    private val dateFormat: DateFormat = SimpleDateFormat("d MMM • HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(MessageView(parent.context, viewType, dateFormat))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = item(position)
        scope.launch { holder.bind(item) }
    }

    override fun getItemViewType(position: Int): Int =
        if (item(position).isAccountMessage)
            Gravity.RIGHT else
            Gravity.LEFT

    private object Diff : DiffUtil.ItemCallback<MessagePresenter>() {
        override fun areItemsTheSame(
            oldItem: MessagePresenter,
            newItem: MessagePresenter
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MessagePresenter,
            newItem: MessagePresenter
        ) = oldItem.message == newItem.message
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val view get() = itemView as MessageView

        suspend fun bind(present: MessagePresenter?) {
            present?.run { view() }
        }
    }

    private fun item(position: Int) = getItem(position)
        ?: throw Exception("cannot get item of position $position, current size: $itemCount")
}