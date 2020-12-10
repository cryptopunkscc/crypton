package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.smack.SmackCore
import org.jxmpp.jid.parts.Localpart

class AccountNet internal constructor(
    smack: SmackCore
) : SmackCore by smack,
    Account.Net {

    override fun createAccount() {
        accountManager.createAccount(
            Localpart.from(configuration.username.toString()),
            configuration.password
        )
    }

    override fun removeAccount() {
        accountManager.deleteAccount()
    }

    override fun login() {
        connection.login()
        carbonManager.enableCarbons()
    }

    override fun isAuthenticated() =
        connection.isAuthenticated

}
