package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.invoke
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.chat_message_item.*
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MessageAdapter @Inject constructor(
    private val scope: CoroutineScope
) :
    PagedListAdapter<MessagePresenter, MessageAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.chat_message_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

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

    inner class ViewHolder(view: View
    ) : RecyclerView.ViewHolder(view),
        LayoutContainer {

        override val containerView: View get() = itemView
        private val scope = this@MessageAdapter.scope + Job()
        private val view = object : MessagePresenter.View {

            override fun setAuthor(name: String) {
                authorTextView.text = name
            }

            override fun setMessage(text: String) {
                bodyTextView.text = text
            }

            override fun setDate(timestamp: Long) {
                timestampTextView.text = Date(timestamp).toString()
            }

            fun clear() {
                setMessage("")
                setDate(0)
            }
        }

        fun bind(present: MessagePresenter?): Unit = scope.run {
            coroutineContext.cancelChildren()
            launch { present(view) ?: view.clear() }
        }
    }
}