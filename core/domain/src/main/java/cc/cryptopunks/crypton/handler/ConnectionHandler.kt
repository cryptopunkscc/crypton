package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.ConnectorOutput
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.addAccount

internal fun handleLogin() = handle { _, _: Account.Service.Login ->
    if (!isConnected()) connect()
    if (!isAuthenticated()) login()
}

internal fun handleAddAccount() = handleConnection<Account.Service.Add>()

internal fun handleRegisterAccount() = handleConnection<Account.Service.Register>()

private fun <C : Account.Service.Connect> handleConnection() = handle { out, arg: C ->
    log.d("Handle $arg")
    when (arg) {
        is Account.Service.Add -> arg.account
        is Account.Service.Register -> arg.account
        else -> throw Exception("Invalid connect")
    }.connectAccount(out) { account ->
        addAccount(
            account = account,
            register = arg is Account.Service.Register,
            insert = true
        )
    }
}

private suspend fun Account.connectAccount(
    out: ConnectorOutput,
    connect: suspend (Account) -> Unit
) {
    Account.Service.Connecting(address).out()
    try {
        connect(this)
        Account.Service.Connected(address)
    } catch (e: Throwable) {
        e.printStackTrace()
        Account.Service.Error(address, e.message)
    }.out()
}
