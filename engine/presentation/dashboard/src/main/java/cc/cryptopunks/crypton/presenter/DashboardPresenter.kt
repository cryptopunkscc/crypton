package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.context.Presenter
import cc.cryptopunks.crypton.context.Route
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
    private val navigate: Route.Api.Navigate
) : Presenter<DashboardPresenter.Actor> {

    override suspend fun Actor.invoke() = coroutineScope {
        launch { accountManagementClick.collect { navigate(Route.AccountManagement) } }
        launch { createChatClick.collect { navigate(Route.CreateChat) } }
    }

    interface Actor {
        val createChatClick: Flow<Any>
        val accountManagementClick: Flow<Any>
    }

    interface Core {
        val dashboardPresenter: DashboardPresenter
    }
}