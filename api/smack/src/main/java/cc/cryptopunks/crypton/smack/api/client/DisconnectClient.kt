package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class DisconnectClient(
    connection: XMPPTCPConnection
) : Account.Api.Disconnect, () -> Unit by {
    connection.disconnect()
}