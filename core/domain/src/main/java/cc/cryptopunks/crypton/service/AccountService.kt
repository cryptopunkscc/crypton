package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleAccountListSubscription
import cc.cryptopunks.crypton.handler.handleAddAccount
import cc.cryptopunks.crypton.handler.handleEnableAccount
import cc.cryptopunks.crypton.handler.handleGetAccountList
import cc.cryptopunks.crypton.handler.handleLogin
import cc.cryptopunks.crypton.handler.handleLogout
import cc.cryptopunks.crypton.handler.handleRegisterAccount
import cc.cryptopunks.crypton.handler.handleRemoveAccount
import cc.cryptopunks.crypton.handler.handleSetField
import cc.cryptopunks.crypton.model.Form
import cc.cryptopunks.crypton.HandlerRegistryFactory
import cc.cryptopunks.crypton.util.Store

val accountHandlers: HandlerRegistryFactory<AppScope> = {
    log.d("Init account service")
    createHandlers {
        val form = Form()
        val lastAccounts = Store(Account.Service.Accounts(emptyList()))

        +handleSetField(form)
        +handleRegisterAccount(form)
        +handleAddAccount(form)
        +handleLogin(form)
        +handleLogout()
        +handleEnableAccount()
        +handleRemoveAccount()
        +handleGetAccountList(lastAccounts)
        +handleAccountListSubscription(lastAccounts)
    }
}
