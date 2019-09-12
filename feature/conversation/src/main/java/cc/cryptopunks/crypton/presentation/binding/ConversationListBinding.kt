package cc.cryptopunks.crypton.presentation.binding

import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.domain.interactor.LoadMessages
import cc.cryptopunks.crypton.presentation.adapter.ConversationItemAdapter
import cc.cryptopunks.crypton.presentation.viewmodel.ConversationListViewModel
import cc.cryptopunks.crypton.util.AsyncExecutor
import cc.cryptopunks.crypton.util.BaseFragment
import kotlinx.android.synthetic.main.conversations.*
import javax.inject.Inject

class ConversationListBinding @Inject constructor(
    loadMessages: LoadMessages,
    async: AsyncExecutor,
    private val conversationListViewModel: ConversationListViewModel
) {

    init {
        async(task = loadMessages)
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

            viewDisposable.addAll(
                conversationItemAdapter,
                conversationItemAdapter.bind(conversationListViewModel.pagedItems)
            )
        }
    }
}