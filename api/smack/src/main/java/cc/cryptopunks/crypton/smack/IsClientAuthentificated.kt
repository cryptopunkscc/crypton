package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.ApiScope
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import javax.inject.Inject

@ApiScope
class IsClientAuthentificated @Inject constructor(
    connection: XMPPTCPConnection
) : Client.IsAuthenticated, () -> Boolean by {
    connection.isAuthenticated
}