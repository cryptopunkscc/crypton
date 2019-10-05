package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart

class CreateClient(
    configuration: XMPPTCPConnectionConfiguration,
    accountManager: AccountManager
) : Client.Create, () -> Unit by {
    accountManager.createAccount(
        Localpart.from(configuration.username.toString()),
        configuration.password
    )
}