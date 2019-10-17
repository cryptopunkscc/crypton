package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.ChatUserListAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.feature.chat.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import kotlinx.android.synthetic.main.create_chat.view.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class CreateChatView(context: Context) :
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

    override suspend fun init() = userEditText.textChanges().map { null }.collect(setError)

    override val createChatClick: Flow<Any>
        get() = inputButton.clicks().map { createUserFromInput() }

    private fun createUserFromInput() = User(userEditText.text.toString())


    override val setUsers: suspend (List<User>) -> Unit
        get() = { userListAdapter.users = it }

    override val clearInput: suspend (Any) -> Unit
        get() = { userEditText.text = null }

    override val setError: suspend (Throwable?) -> Unit
        get() = { throwable -> errorOutput.text = throwable?.message }

    override val addUserClick: Flow<User> get() = emptyFlow()

    override val removeUserClick: Flow<User> get() = emptyFlow()
}