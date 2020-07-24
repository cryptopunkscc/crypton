package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.interactor.addAccount
import cc.cryptopunks.crypton.model.Form


internal fun handleLogin() = handle { _, _: Account.Service.Login ->
    if (!isConnected()) connect()
    if (!isAuthenticated()) login()
}

internal fun handleAddAccount(form: Form) = handleConnection<Account.Service.Add>(form)

internal fun handleRegisterAccount(form: Form) = handleConnection<Account.Service.Register>(form)

private fun <C : Account.Service.Connect> handleConnection(
    form: Form
) =
    handle { out, arg: C ->
        log.d("Handle $arg")
        when (arg) {
            is Account.Service.Add -> arg.account ?: form.account()
            is Account.Service.Register -> arg.account ?: form.account()
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
    out: suspend (Any) -> Unit,
    connect: suspend (Account) -> Unit
) {
    out(Account.Service.Connecting(address))
    try {
        connect(this)
        out(Account.Service.Connected(address))
    } catch (e: Throwable) {
        e.printStackTrace()
        out(Account.Service.Error(address, e.message))
    }
}

private fun Form.account() = Account(
    address = Address(
        local = get()[Account.Field.UserName].toString(),
        domain = get()[Account.Field.ServiceName].toString()
    ),
    password = Password(get()[Account.Field.Password]!!)
)
