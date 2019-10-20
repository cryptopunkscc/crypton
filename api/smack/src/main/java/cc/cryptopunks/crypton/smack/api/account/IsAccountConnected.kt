package cc.cryptopunks.crypton.smack.api.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsAccountConnected(
    private val connection: XMPPTCPConnection
) : Account.Api.IsConnected, () -> Boolean by {
    connection.isConnected
}