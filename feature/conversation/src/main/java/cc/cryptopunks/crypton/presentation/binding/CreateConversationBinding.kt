package cc.cryptopunks.crypton.presentation.binding

import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.presentation.adapter.ConversationUserListAdapter
import cc.cryptopunks.crypton.presentation.viewmodel.CreateConversationViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import cc.cryptopunks.crypton.util.bind
import com.jakewharton.rxbinding3.widget.editorActions
import kotlinx.android.synthetic.main.create_conversation.*
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
            viewDisposable.addAll(
                userListAdapter(model.users),
                conversationUserEditText.bind(model.userInput),
                conversationUserEditText.editorActions()
                    .filter { it == EditorInfo.IME_ACTION_DONE }
                    .subscribe { model.addFromInput() }
            )
        }
    }
}