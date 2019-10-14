package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import kotlinx.android.synthetic.main.roster.view.*
import kotlinx.coroutines.cancelChildren


class RosterView(
    context: Context
) : FrameLayout(context),
    RosterPresenter.View {

    private val scope = Actor.Scope()

    private val rosterAdapter = RosterAdapter(scope)

    init {
        View.inflate(context, R.layout.roster, this)
        rosterRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rosterAdapter
        }
    }

    override fun onDetachedFromWindow() {
        scope.coroutineContext.cancelChildren()
        super.onDetachedFromWindow()
    }


    override val setList: suspend (PagedList<RosterItemPresenter>) -> Unit
        get() = {
            rosterAdapter.submitList(it)
        }
}