package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class DisconnectClient(
    connection: XMPPTCPConnection
) : Account.Net.Disconnect, () -> Unit by {
    connection.disconnect()
}