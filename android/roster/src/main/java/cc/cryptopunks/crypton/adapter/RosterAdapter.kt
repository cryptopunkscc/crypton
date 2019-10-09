package cc.cryptopunks.crypton.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.util.invoke
import cc.cryptopunks.crypton.util.letterColors
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.roster_item.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class RosterAdapter @Inject constructor(
    private val scope: CoroutineScope
) :
    PagedListAdapter<RosterItemPresenter, RosterAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.roster_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    private object Diff : DiffUtil.ItemCallback<RosterItemPresenter>() {
        override fun areItemsTheSame(
            oldItem: RosterItemPresenter,
            newItem: RosterItemPresenter
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RosterItemPresenter,
            newItem: RosterItemPresenter
        ) = areItemsTheSame(oldItem, newItem)
    }

    inner class ViewHolder(
        override val containerView: android.view.View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer,
        CoroutineScope by scope + Job() {

        private val view = object : RosterItemPresenter.View {

            override fun setTitle(title: String) {
                conversationTitleTextView.text = title
            }

            override fun setLetter(letter: Char) {
                conversationLetter.apply {
                    text = letter.toString()
                    setBackgroundResource(letterColors.getValue(letter))
                }
            }

            override val setMessage: suspend (Message) -> Unit = { message ->
                lastMessageTextView.text = message.text
                dateTextView.text = Date(message.timestamp).toString()
            }

            override val onClick: Flow<Any> = containerView.clicks()

            fun clear() {
                setLetter('a')
                setTitle("")
            }
        }

        fun bind(present: RosterItemPresenter?) {
            coroutineContext.cancelChildren()
            launch { present(view) ?: view.clear() }
        }
    }
}