package cc.cryptopunks.crypton.smack.net.client

import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.util.log
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class ConnectClient(
    connection: XMPPTCPConnection
) : Account.Net.Connect, () -> Unit by {
    connection.run {
        if (!isConnected) {
            log<Account.Net.Connect>("connecting")
            connect()
        }
    }
}