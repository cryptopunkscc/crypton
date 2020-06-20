package cc.cryptopunks.crypton.view

import android.view.View
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.navigate.navigateChat
import cc.cryptopunks.crypton.navigate.navigateCreateChat
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DashboardView(
    override val containerView: View
) :
    Actor,
    LayoutContainer {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun Connector.connect(): Job = launch {
        launch {
            input.filterIsInstance<Chat.Service.ChatCreated>().collect { arg ->
                containerView.findNavController().navigateChat(
                    account = containerView.context.currentAccount!!,
                    chat = arg.address
                )
            }
        }
        launch {
            createConversationButton.clicks().map {
                containerView.findNavController().navigateCreateChat()
            }.collect(output)
        }
    }
}
