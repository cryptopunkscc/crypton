package cc.cryptopunks.crypton.presentation.binding

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.presentation.adapter.ConversationUserListAdapter
import cc.cryptopunks.crypton.presentation.adapter.bind
import cc.cryptopunks.crypton.feature.chat.viewmodel.CreateChatViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.crypton.util.reactivebindings.bind
import kotlinx.android.synthetic.main.create_conversation.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

class CreateChatBinding @Inject constructor(
    private val model: CreateChatViewModel,
    private val onOptionItemSelected: OptionItemSelected.Output
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
            launch { conversationUserEditText.bind(model.input) }
            launch { onOptionItemSelected.collect { model.createChat() } }
            conversationUserEditText.setOnEditorActionListener { _, actionId, _ ->
                (actionId == EditorInfo.IME_ACTION_DONE).also {
                    if (it) model.addFromInput()
                }
            }
        }
    }
}