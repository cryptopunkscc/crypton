package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import javax.inject.Inject

@ApiScope
class ConnectClient @Inject constructor(
    connection: XMPPTCPConnection
) : Client.Connect, () -> Unit by {
    connection.run {
        if (!isConnected)
            connect()
    }
}