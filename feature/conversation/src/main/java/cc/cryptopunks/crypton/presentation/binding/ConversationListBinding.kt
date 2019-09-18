package cc.cryptopunks.crypton.presentation.binding

import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.presentation.adapter.ConversationItemAdapter
import cc.cryptopunks.crypton.presentation.adapter.bind
import cc.cryptopunks.crypton.presentation.viewmodel.ConversationListViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import kotlinx.android.synthetic.main.conversations.*
import kotlinx.coroutines.async
import javax.inject.Inject

class ConversationListBinding @Inject constructor(
    private val conversationListViewModel: ConversationListViewModel
) {

    init {
        conversationListViewModel()
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
                conversationItemAdapter.bind(conversationListViewModel.pagedItems)
            }
        }
    }
}