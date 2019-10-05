package cc.cryptopunks.crypton.presentation.binding

import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.presentation.adapter.ConversationItemAdapter
import cc.cryptopunks.crypton.presentation.adapter.bind
import cc.cryptopunks.crypton.feature.chat.viewmodel.RosterViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import kotlinx.android.synthetic.main.conversations.*
import kotlinx.coroutines.async
import javax.inject.Inject

class RosterBinding @Inject constructor(
    private val rosterViewModel: RosterViewModel
) {

    init {
        rosterViewModel()
    }

    inner class ViewBinding {
        @Inject
        fun BaseFragment.bind(
            conversationItemAdapter: ConversationItemAdapter
        ) {

            conversationRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = conversationItemAdapter
            }
            async {
                conversationItemAdapter.bind(rosterViewModel.pagedItems)
            }
        }
    }
}