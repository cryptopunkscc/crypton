package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.context.Net
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class InterruptNet(
    connection: XMPPTCPConnection
) : Net.Interrupt, () -> Unit by {
    connection.instantShutdown()
}