package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.addAccount

internal fun handleLogin() = handle { _, _: Exec.Connect ->
    if (!isConnected()) connect()
    if (!isAuthenticated()) login()
}

internal fun handleAddAccount() = handleConnection<Exec.Login>()

internal fun handleRegisterAccount() = handleConnection<Exec.Register>()

private fun <C : Exec.Authenticate> handleConnection() = handle { out, arg: C ->
    log.d("Handle $arg")
    when (arg) {
        is Exec.Login -> arg.account
        is Exec.Register -> arg.account
        else -> throw Exception("Invalid connect")
    }.connectAccount(out) { account ->
        addAccount(
            account = account,
            register = arg is Exec.Register,
            insert = true
        )
    }
}

private suspend fun Account.connectAccount(
    out: Output,
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
