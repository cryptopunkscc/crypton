package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Account

class MockAccountNet : Account.Net {
    override fun createAccount() = throw NotImplementedError()
    override fun removeAccount() = throw NotImplementedError()
    override fun login() = Unit
    override fun isAuthenticated() = true
}
