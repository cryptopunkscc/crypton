package cc.cryptopunks.crypton.fragment

import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import cc.cryptopunks.crypton.util.Scope
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.roster.*
import javax.inject.Inject

class RosterView @Inject constructor(
    override var containerView: View,
    scope: Scope.View
) : RosterPresenter.View,
    LayoutContainer {

    private val rosterAdapter: RosterAdapter = RosterAdapter(scope)

    init {
        rosterRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rosterAdapter
        }
    }

    override val setList: suspend (PagedList<RosterItemPresenter>) -> Unit get() = {
        rosterAdapter.submitList(it)
    }
}