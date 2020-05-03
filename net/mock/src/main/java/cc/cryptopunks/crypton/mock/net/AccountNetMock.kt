package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Account

class AccountNetMock : Account.Net {
    override val createAccount: Account.Net.Create get() = throw NotImplementedError()
    override val removeAccount: Account.Net.Remove get() = throw NotImplementedError()
    override val login: Account.Net.Login = object : Account.Net.Login, () -> Unit by {} {}
    override val isAuthenticated: Account.Net.IsAuthenticated =
        object : Account.Net.IsAuthenticated, () -> Boolean by { true } {}
}
