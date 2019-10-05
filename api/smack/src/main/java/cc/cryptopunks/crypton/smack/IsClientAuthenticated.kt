package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsClientAuthenticated(
    connection: XMPPTCPConnection
) : Client.IsAuthenticated, () -> Boolean by {
    connection.isAuthenticated
}