package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Account

class AccountNetMock : Account.Net {
    override fun createAccount() = throw NotImplementedError()
    override fun removeAccount() = throw NotImplementedError()
    override fun login() = Unit
    override fun isAuthenticated() = true
}
