package cc.cryptopunks.crypton.smack.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class LoginClient(
    connection: XMPPTCPConnection
) : Client.Login, () -> Unit by {
    connection.login()
}