package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun createConversation() {
        navigate(R.id.navigateCreateChat)
    }
}