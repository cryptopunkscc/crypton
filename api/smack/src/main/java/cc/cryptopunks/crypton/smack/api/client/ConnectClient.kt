package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.util.log
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectClient(
    connection: XMPPTCPConnection
) : Client.Connect, () -> Unit by {
    connection.run {
        if (!isConnected) {
            Client.Connect::class.log("connecting")
            connect()
        }
    }
}