package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.context.Account
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart

class CreateAccount(
    configuration: XMPPTCPConnectionConfiguration,
    accountManager: AccountManager
) : Account.Net.Create, () -> Unit by {
    accountManager.createAccount(
        Localpart.from(configuration.username.toString()),
        configuration.password
    )
}

fun SASLErrorException.getErrorMessage() = saslFailure.saslError.toString().replace("_", " ")