package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectClient(
    connection: XMPPTCPConnection
) : Client.Connect, () -> Unit by {
    connection.run {
        if (!isConnected)
            connect()
    }
}