package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleAdd
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleRegister
import cc.cryptopunks.crypton.handler.handleRemove
import cc.cryptopunks.crypton.handler.handleSetField
import cc.cryptopunks.crypton.model.Form
import cc.cryptopunks.crypton.selector.newAccountConnectedFlow
import cc.cryptopunks.crypton.util.service
import kotlinx.coroutines.flow.collect

fun accountService(scope: AppScope) = service(scope) {
    createAccountHandlers()
}

fun AppScope.createAccountHandlers() = createHandlers {
    val form = Form()

    +handleSetField(form)
    +handleRegister(form)
    +handleAdd(form)
    +handleLogin(form)
    +handleLogout()
    +handleRemove()
    +handleAccountListSubscription()
}


suspend fun AppScope.handleAccountConnections() = newAccountConnectedFlow().collect {
    navigate(Route.AccountList)
}
