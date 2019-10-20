package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsAccountConnected(
    private val connection: XMPPTCPConnection
) : Account.Net.IsConnected, () -> Boolean by {
    connection.isConnected
}