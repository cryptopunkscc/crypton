package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.ApiScope
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart
import javax.inject.Inject

@ApiScope
class CreateClient @Inject constructor(
    configuration: XMPPTCPConnectionConfiguration,
    accountManager: AccountManager
) : Client.Create, () -> Unit by {
    accountManager.createAccount(
        Localpart.from(configuration.username.toString()),
        configuration.password
    )
}