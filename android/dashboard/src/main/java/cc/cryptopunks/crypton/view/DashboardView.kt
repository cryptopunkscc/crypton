package cc.cryptopunks.crypton.view

import android.view.View
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DashboardView(
    override val containerView: View
) :
    Actor,
    LayoutContainer {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun Connector.connect(): Job = launch {
        createConversationButton.clicks().map { Route.CreateChat }.collect(output)
    }
}
