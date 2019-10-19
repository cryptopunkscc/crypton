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
        ViewHolder(MessageView(parent.context, dateFormat).setGravity(viewType))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        scope.launch { holder.bind(getItem(position)) }
    }

    override fun getItemViewType(position: Int): Int =
        if (getItem(position)!!.isAccountMessage)
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
        ) = areItemsTheSame(oldItem, newItem)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val view get() = itemView as MessageView

        suspend fun bind(present: MessagePresenter?) {
            present?.run { view() }
        }
    }
}