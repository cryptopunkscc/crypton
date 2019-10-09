package cc.cryptopunks.crypton.fragment

import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.roster.*
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class RosterView @Inject constructor(
    override val containerView: View,
    scope: CoroutineScope
) : RosterPresenter.View,
    LayoutContainer {

    private val rosterAdapter = RosterAdapter(scope)

    init {
        rosterRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rosterAdapter
        }
    }

    override val setList: suspend (PagedList<RosterItemPresenter>) -> Unit = {
        rosterAdapter.submitList(it)
    }
}