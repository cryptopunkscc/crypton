package cc.cryptopunks.crypton.smack.api.account

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsAccountConnected(
    private val connection: XMPPTCPConnection
) : Client.IsConnected, () -> Boolean by {
    connection.isConnected
}