package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.net.Net
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class DisconnectClient(
    connection: XMPPTCPConnection
) : Net.Disconnect, () -> Unit by {
    connection.disconnect()
}