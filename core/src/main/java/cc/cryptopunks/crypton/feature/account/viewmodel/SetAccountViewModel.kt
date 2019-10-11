package cc.cryptopunks.crypton.feature.account.viewmodel

import cc.cryptopunks.crypton.navigation.Navigate
import cc.cryptopunks.crypton.navigation.Route
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun addAccount(): Unit = navigate(Route.Login)

    fun registerAccount(): Unit = navigate(Route.Register)
}