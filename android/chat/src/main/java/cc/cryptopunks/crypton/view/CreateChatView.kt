package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.ChatUserListAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.presenter.CreateChatService
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import kotlinx.android.synthetic.main.create_chat.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateChatView(context: Context) :
    FrameLayout(context),
    Service.Wrapper {

    private val scope = Actor.Scope()

    override val wrapper = wrapper(scope)

    private val userListAdapter =
        ChatUserListAdapter()

    init {
        View.inflate(
            context,
            R.layout.create_chat,
            this
        )
        userListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
        }
        scope.run {
            launch { addressInputView.input.textChanges().collect { setError(null) } }
            launch { addressInputView.button.clicks().collect { createUserFromInput().out() } }
        }
    }

    private fun createUserFromInput() = CreateChatService.Input.CreateChat(
        Address.from(addressInputView.input.text.toString())
    )

    private fun setError(throwable: Throwable?) {
        errorOutput.text = throwable?.message
    }
}