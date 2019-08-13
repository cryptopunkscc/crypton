package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.XmppScope
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jivesoftware.smackx.iqregister.AccountManager
import org.jxmpp.jid.parts.Localpart
import javax.inject.Inject

@XmppScope
class XmppCreate @Inject constructor(
    configuration: XMPPTCPConnectionConfiguration,
    accountManager: AccountManager
) : Xmpp.Create, () -> Unit by {
    accountManager.createAccount(
        Localpart.from(configuration.username.toString()),
        configuration.password
    )
}