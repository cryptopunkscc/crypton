package cc.cryptopunks.crypton.smack.api.client

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.log
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectClient(
    connection: XMPPTCPConnection
) : Account.Api.Connect, () -> Unit by {
    connection.run {
        if (!isConnected) {
            log<Account.Api.Connect>("connecting")
            connect()
        }
    }
}