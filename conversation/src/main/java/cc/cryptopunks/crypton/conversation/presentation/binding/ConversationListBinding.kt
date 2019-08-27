package cc.cryptopunks.crypton.conversation.presentation.binding

import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.conversation.domain.command.LoadMessages
import cc.cryptopunks.crypton.conversation.presentation.adapter.ConversationItemAdapter
import cc.cryptopunks.crypton.conversation.presentation.viewmodel.ConversationListViewModel
import cc.cryptopunks.crypton.core.util.BaseFragment
import cc.cryptopunks.crypton.core.util.ModelDisposable
import kotlinx.android.synthetic.main.conversations.*
import javax.inject.Inject

class ConversationListBinding @Inject constructor(
    modelDisposable: ModelDisposable,
    loadMessages: LoadMessages,
    private val conversationListViewModel: ConversationListViewModel
) {

    init {
        modelDisposable.addAll(
            loadMessages().subscribe()
        )
    }

    inner class ViewBinding {
        @Inject
        fun BaseFragment.bind(
            conversationItemAdapter: ConversationItemAdapter
        ) {

            accountRecyclerView.apply {
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