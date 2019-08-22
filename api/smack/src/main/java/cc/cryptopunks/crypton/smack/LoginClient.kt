package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.ApiScope
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import javax.inject.Inject

@ApiScope
class LoginClient @Inject constructor(
    connection: XMPPTCPConnection
) : Client.Login, () -> Unit by {
    connection.login()
}