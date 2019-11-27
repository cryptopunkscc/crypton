package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.context.Net
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsAccountConnected(
    private val connection: XMPPTCPConnection
) : Net.IsConnected, () -> Boolean by {
    connection.isConnected
}