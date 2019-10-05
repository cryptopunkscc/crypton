package cc.cryptopunks.crypton.feature.account.viewmodel

import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun addAccount(): Unit = navigate(Route.Login)

    fun registerAccount(): Unit = navigate(Route.Register)
}