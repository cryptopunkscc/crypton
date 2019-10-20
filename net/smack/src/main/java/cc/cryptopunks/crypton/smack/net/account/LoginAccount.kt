package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.entity.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class LoginAccount(
    connection: XMPPTCPConnection
) : Account.Net.Login, () -> Unit by {
    connection.login()
}