package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.adapter.ChatUserListAdapter
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Service
import cc.cryptopunks.crypton.service.CreateChatService
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ServiceLayout
import kotlinx.android.synthetic.main.create_chat.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateChatView(context: Context) :
    ServiceLayout(context) {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    private val userListAdapter = ChatUserListAdapter()

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
    }

    override fun Service.Binding.bind(): Job = launch {
        launch {
            addressInputView.input.textChanges().collect {
                setError(null)
            }
        }
        launch {
            addressInputView.button.clicks().collect {
                createUserFromInput().out()
            }
        }
    }

    private fun createUserFromInput() = CreateChatService.CreateChat(
        Address.from(addressInputView.input.text.toString())
    )

    private fun setError(throwable: Throwable?) {
        errorOutput.text = throwable?.message
    }
}