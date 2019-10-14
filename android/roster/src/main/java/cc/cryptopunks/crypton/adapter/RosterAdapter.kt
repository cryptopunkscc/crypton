package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.view.RosterItemView
import kotlinx.coroutines.*
import javax.inject.Inject

class RosterAdapter @Inject constructor(
    private val scope: Actor.Scope
) :
    PagedListAdapter<RosterItemPresenter, RosterAdapter.ViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(RosterItemView(parent.context))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        scope.launch { holder.bind(getItem(position)) }
    }

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

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.cancel()
    }

    inner class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        var job: Job? = null

        private val view get() = itemView as RosterItemView

        suspend fun bind(present: RosterItemPresenter?) {
            job?.cancel()
            job = present?.run { view.invoke() }
            job ?: view.clear()
        }

        fun cancel() {
            job?.cancel()
            job = null
        }
    }
}