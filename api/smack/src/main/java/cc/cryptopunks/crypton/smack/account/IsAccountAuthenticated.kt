package cc.cryptopunks.crypton.smack.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsAccountAuthenticated(
    connection: XMPPTCPConnection
) : Account.Api.IsAuthenticated, () -> Boolean by {
    connection.isAuthenticated
}