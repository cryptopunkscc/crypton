package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.Xmpp
import cc.cryptopunks.crypton.xmpp.XmppScope
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import javax.inject.Inject

@XmppScope
class XmppIsAuthentificated @Inject constructor(
    connection: XMPPTCPConnection
) : Xmpp.IsAuthenticated, () -> Boolean by {
    connection.isAuthenticated
}