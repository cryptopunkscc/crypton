package cc.cryptopunks.crypton.presenter

import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
    private val navigate: Navigate
) : Presenter<DashboardPresenter.View> {

    override suspend fun View.invoke() = coroutineScope {
        launch { accountManagementClick.collect { navigate(Route.AccountManagement) } }
        launch { createChatClick.collect { navigate(Route.CreateChat) } }
    }

    interface View : Actor {
        val createChatClick: Flow<Any>
        val accountManagementClick: Flow<Any>
    }
}