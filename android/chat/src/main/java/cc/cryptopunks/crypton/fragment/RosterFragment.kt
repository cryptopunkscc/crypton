package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import cc.cryptopunks.crypton.adapter.RosterAdapter
import cc.cryptopunks.crypton.util.invoke
import kotlinx.android.synthetic.main.roster.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterFragment : RosterComponentFragment() {

    override val layoutRes: Int get() = R.layout.roster

    @Inject
    lateinit var present: RosterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)
        launch { present() }
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        launch { present(View()) }
    }

    inner class View : RosterPresenter.View {

        private val rosterAdapter = RosterAdapter(this@RosterFragment)

        init {
            rosterRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = rosterAdapter
            }
        }

        override val setList: suspend (PagedList<RosterItemPresenter>) -> Unit = {
            rosterAdapter.submitList(it)
        }
    }
}