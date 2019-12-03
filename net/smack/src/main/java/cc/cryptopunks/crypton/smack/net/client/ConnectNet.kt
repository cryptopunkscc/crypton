package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.d
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectNet(
    connection: XMPPTCPConnection
) : Net.Connect, () -> Unit by {
    connection.run {
        if (!isConnected) {
            Log.d<ConnectNet>("connecting")
            connect()
            Log.d<ConnectNet>("connected")
        }
    }
}