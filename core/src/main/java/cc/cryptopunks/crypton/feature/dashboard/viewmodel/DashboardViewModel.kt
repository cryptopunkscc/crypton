package cc.cryptopunks.crypton.feature.dashboard.viewmodel

import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun createConversation() {
        navigate(Route.CreateChat)
    }
}