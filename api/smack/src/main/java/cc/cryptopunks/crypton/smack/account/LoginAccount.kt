package cc.cryptopunks.crypton.smack.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class LoginAccount(
    connection: XMPPTCPConnection
) : Account.Api.Login, () -> Unit by {
    connection.login()
}