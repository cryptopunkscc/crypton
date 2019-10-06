package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.feature.chat.presenter.RosterItemPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.RosterPresenter
import cc.cryptopunks.crypton.presentation.adapter.ChatItemAdapter
import cc.cryptopunks.crypton.util.invoke
import kotlinx.android.synthetic.main.chat.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterFragment : ChatComponentFragment() {

    override val layoutRes: Int get() = R.layout.chat

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

        private val chatItemAdapter = ChatItemAdapter(this@RosterFragment)

        init {
            rosterRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = chatItemAdapter
            }
        }

        override val setList: suspend (PagedList<RosterItemPresenter>) -> Unit = {
            chatItemAdapter.submitList(it)
        }
    }
}