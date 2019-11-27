package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.log
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectClient(
    connection: XMPPTCPConnection
) : Net.Connect, () -> Unit by {
    connection.run {
        if (!isConnected) {
            log<Net.Connect>("connecting")
            connect()
        }
    }
}