package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.ChatUserListAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.feature.chat.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.util.bindings.flowEditorActions
import kotlinx.android.synthetic.main.create_chat.view.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class CreateChatView(
    context: Context,
    override val createChatClick: Flow<Any>
) :
    FrameLayout(context),
    CreateChatPresenter.View {

    private val userListAdapter = ChatUserListAdapter()

    init {
        View.inflate(context, R.layout.create_chat, this)
        userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
    }

    override val addUserClick: Flow<String>
        get() = userEditText
            .flowEditorActions()
            .filter { it.id == EditorInfo.IME_ACTION_DONE }
            .map { it.view.text.toString() }

    override val removeUserClick: Flow<User> get() = emptyFlow() // TODO

    override val setUsers: suspend (List<User>) -> Unit
        get() = {
            userListAdapter.users = it
        }

    override val clearInput: suspend (Any) -> Unit
        get() = {
            userEditText.text = null
        }
}