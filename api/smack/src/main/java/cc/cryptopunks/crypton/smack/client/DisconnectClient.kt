package cc.cryptopunks.crypton.smack.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class DisconnectClient(
    connection: XMPPTCPConnection
) : Client.Disconnect, () -> Unit by {
    connection.disconnect()
}