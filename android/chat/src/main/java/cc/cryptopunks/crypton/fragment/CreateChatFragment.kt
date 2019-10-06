package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.feature.chat.presenter.CreateChatPresenter
import cc.cryptopunks.crypton.adapter.ChatUserListAdapter
import cc.cryptopunks.crypton.util.invoke
import kotlinx.android.synthetic.main.create_chat.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateChatFragment :
    ChatComponentFragment() {

    override val layoutRes: Int get() = R.layout.create_chat

    @Inject
    lateinit var present: CreateChatPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        component.inject(this)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        launch { present(View()) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_chat, menu)
    }

    private inner class View : CreateChatPresenter.View {
        private val userListAdapter = ChatUserListAdapter()

        init {
            userListRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = userListAdapter
            }
        }

        override val addUserClick: Flow<String> = callbackFlow {
            userEditText.setOnEditorActionListener { view, actionId, _ ->
                (actionId == EditorInfo.IME_ACTION_DONE).also {
                    if (it) channel.offer(view.text.toString())
                }
            }
            awaitClose()
        }

        override val removeUserClick: Flow<User> = emptyFlow() // TODO
        override val createChatClick: Flow<Any> = component.optionItemSelections
        override val setUsers: suspend (List<User>) -> Unit = { userListAdapter.users = it }
        override val clearInput: suspend (Any) -> Unit = { userEditText.text = null }
    }
}