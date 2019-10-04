package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.R
import cc.cryptopunks.crypton.dagger.ViewModelScope
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

@ViewModelScope
class DashboardViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun createConversation() {
        navigate(R.id.navigateCreateChat)
    }
}