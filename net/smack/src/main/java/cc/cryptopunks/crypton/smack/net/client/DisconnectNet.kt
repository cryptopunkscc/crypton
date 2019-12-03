package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.d
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class DisconnectNet(
    connection: XMPPTCPConnection
) : Net.Disconnect, () -> Unit by {
    Log.d<DisconnectNet>("disconnecting")
    connection.disconnect()
    Log.d<DisconnectNet>("disconnected")
}