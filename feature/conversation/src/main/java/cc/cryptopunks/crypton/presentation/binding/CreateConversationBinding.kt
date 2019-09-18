package cc.cryptopunks.crypton.presentation.binding

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.presentation.adapter.ConversationUserListAdapter
import cc.cryptopunks.crypton.presentation.adapter.bind
import cc.cryptopunks.crypton.presentation.viewmodel.CreateConversationViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import cc.cryptopunks.crypton.util.reactivebindings.bind
import kotlinx.android.synthetic.main.create_conversation.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

class CreateConversationBinding @Inject constructor(
    private val model: CreateConversationViewModel
) {

    inner class ViewBinding {
        @Inject
        fun BaseFragment.bind(
            userListAdapter: ConversationUserListAdapter
        ) {
            userListRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = userListAdapter
            }
            launch { userListAdapter.bind(model.users.asFlow()) }
            launch { conversationUserEditText.bind(model.userInput) }
            conversationUserEditText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    model.addFromInput()
                    true
                } else
                    false
            }
        }
    }
}