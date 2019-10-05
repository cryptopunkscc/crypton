package cc.cryptopunks.crypton.feature.dashboard.viewmodel

import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun createConversation() {
        navigate(Route.CreateChat)
    }
}