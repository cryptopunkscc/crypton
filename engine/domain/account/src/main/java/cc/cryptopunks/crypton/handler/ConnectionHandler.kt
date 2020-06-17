package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.interactor.addAccount
import cc.cryptopunks.crypton.service.Form
import kotlinx.coroutines.launch

internal fun AppScope.handleLogin(form: Form) =
    handleConnection<Account.Service.Login>(form)

internal fun AppScope.handleRegister(form: Form) =
    handleConnection<Account.Service.Register>(form)

private fun <C : Account.Service.Connect> AppScope.handleConnection(
    form: Form
) = handle<C> { out ->
    launch {
        form.account().let { account ->
            out(Account.Service.Connecting(account.address))
            try {
                addAccount(
                    account = account,
                    register = this@handle is Account.Service.Register
                )
                out(Account.Service.Connected(account.address))
            } catch (e: Throwable) {
                e.printStackTrace()
                out(Account.Service.Error(account.address, e.message))
            }
        }
    }
}

private fun Form.account() = Account(
    address = Address(
        local = get()[Account.Field.UserName].toString(),
        domain = get()[Account.Field.ServiceName].toString()
    ),
    password = Password(get()[Account.Field.Password]!!)
)
