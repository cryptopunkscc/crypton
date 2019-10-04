package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import javax.inject.Inject

@ApiScope
class DisconnectClient @Inject constructor(
    connection: XMPPTCPConnection
) : Client.Disconnect, () -> Unit by {
    connection.disconnect()
}