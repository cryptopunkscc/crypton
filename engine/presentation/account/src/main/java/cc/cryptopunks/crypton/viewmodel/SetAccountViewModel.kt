package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.navigation.Route
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    private val navigate: Route.Api.Navigate
) {

    fun addAccount(): Unit = navigate(Route.Login)

    fun registerAccount(): Unit = navigate(Route.Register)
}