package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.addAccount
import cc.cryptopunks.crypton.model.Form

internal fun AppScope.handleAddAccount(form: Form) =
    handleConnection<Account.Service.Add>(form)

internal fun AppScope.handleRegisterAccount(form: Form) =
    handleConnection<Account.Service.Register>(form)

internal fun AppScope.handleLogin(form: Form) =
    handleConnection<Account.Service.Login>(form)

private fun <C : Any> AppScope.handleConnection(
    form: Form
) =
    handle<C> { out ->
        log.d("Handle $this")
        when (this@handle) {
            is Account.Service.Add -> account ?: form.account()
            is Account.Service.Register -> account ?: form.account()
            is Account.Service.Login -> accountRepo.get(address)
            else -> throw Exception("Invalid connect")
        }.connectAccount(out) { account ->
            addAccount(
                account = account,
                register = this@handle is Account.Service.Register,
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
