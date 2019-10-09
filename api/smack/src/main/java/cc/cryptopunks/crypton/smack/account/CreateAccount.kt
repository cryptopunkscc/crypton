package cc.cryptopunks.crypton.smack.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.sasl.SASLErrorException
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart

class CreateAccount(
    configuration: XMPPTCPConnectionConfiguration,
    accountManager: AccountManager
) : Account.Api.Create, () -> Unit by {
    accountManager.createAccount(
        Localpart.from(configuration.username.toString()),
        configuration.password
    )
}

fun SASLErrorException.getErrorMessage() = saslFailure.saslError.toString().replace("_", " ")