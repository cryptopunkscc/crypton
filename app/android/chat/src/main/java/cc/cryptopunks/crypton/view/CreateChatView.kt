package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.navigate.navigateChat
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.bindings.textChanges
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.create_chat.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

class CreateChatView(context: Context) :
    ActorLayout(context) {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    init {
        View.inflate(context, R.layout.create_chat, this)
        addressInputView.slash.visibility = View.GONE
        addressInputView.encrypt.visibility = View.GONE
    }

    override fun Connector.connect(): Job = launch {
        launch {
            input.filterIsInstance<Account.ChatCreated>().collect { created ->
                findNavController().navigateChat(
                    account = context.currentAccount,
                    chat = created.chat
                )
            }
        }
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

    private fun createUserFromInput() = Exec.CreateChat(
        Chat(
            account = context.currentAccount,
            address = address(addressInputView.input.text.toString())
        )
    ).inContext(context.currentAccount)

    private fun setError(throwable: Throwable?) {
        errorOutput.text = throwable?.message
    }
}
