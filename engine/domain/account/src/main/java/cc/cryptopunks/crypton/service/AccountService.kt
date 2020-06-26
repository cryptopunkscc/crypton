package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleAdd
import cc.cryptopunks.crypton.handler.handleEnableAccount
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleRegister
import cc.cryptopunks.crypton.handler.handleRemove
import cc.cryptopunks.crypton.handler.handleSetField
import cc.cryptopunks.crypton.model.Form
import cc.cryptopunks.crypton.util.service

fun accountService(scope: AppScope) = service(scope) {
    scope.log.d("Init account service")
    accountHandlers()
}

fun AppScope.accountHandlers() = createHandlers {
    val form = Form()

    +handleSetField(form)
    +handleRegister(form)
    +handleAdd(form)
    +handleLogin(form)
    +handleLogout()
    +handleEnableAccount()
    +handleRemove()
    +handleAccountListSubscription()
}
