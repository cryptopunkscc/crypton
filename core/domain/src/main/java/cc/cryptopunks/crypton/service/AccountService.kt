package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleAddAccount
import cc.cryptopunks.crypton.handler.handleEnableAccount
import cc.cryptopunks.crypton.handler.handleGetAccountList
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleRegisterAccount
import cc.cryptopunks.crypton.handler.handleRemoveAccount
import cc.cryptopunks.crypton.model.Form
import cc.cryptopunks.crypton.util.Store

fun accountHandlers() = createHandlers {
    val form = Form()
    val lastAccounts = Store(Account.Service.Accounts(emptyList()))

    +handleRegisterAccount(form)
    +handleAddAccount(form)
    +handleLogin()
    +handleLogout()
    +handleEnableAccount()
    +handleRemoveAccount()
    +handleGetAccountList(lastAccounts)
    +handleAccountListSubscription(lastAccounts)
}
