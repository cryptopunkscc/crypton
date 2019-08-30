package cc.cryptopunks.crypton.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.conversation.R
import cc.cryptopunks.crypton.presentation.viewmodel.ConversationItemViewModel
import cc.cryptopunks.crypton.util.Disposables
import cc.cryptopunks.crypton.util.ext.inflate
import io.reactivex.Observable
import io.reactivex.internal.disposables.CancellableDisposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.conversation_item.*
import java.util.*
import javax.inject.Inject

class ConversationItemAdapter @Inject constructor() :
    PagedListAdapter<ConversationItemViewModel, ConversationItemAdapter.ViewHolder>(Diff),
    Disposables by Disposables() {

    fun bind(observable: Observable<PagedList<ConversationItemViewModel>>) =
        observable.subscribe { submitList(it) }!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.conversation_item)).also { add(it) }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer,
        Disposables by Disposables() {

        fun bind(model: ConversationItemViewModel?) {
            clear()
            model?.apply {
                conversationLetter.text = letter.toString()
                conversationLetter.setBackgroundResource(avatarColor)

                conversationTitleTextView.text = title

                addAll(
                    lastMessageObservable.subscribe { message ->
                        lastMessageTextView.text = message.text
                        dateTextView.text = Date(message.timestamp).toString()
                    },
                    CancellableDisposable {
                        conversationLetter.apply {
                            text = ""
                            background = null
                        }
                    }
                )
            }
        }
    }

    private object Diff : DiffUtil.ItemCallback<ConversationItemViewModel>() {
        override fun areItemsTheSame(
            oldItem: ConversationItemViewModel,
            newItem: ConversationItemViewModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ConversationItemViewModel,
            newItem: ConversationItemViewModel
        ) = areItemsTheSame(oldItem, newItem)
    }
}