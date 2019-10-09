package cc.cryptopunks.crypton.smack.client

import cc.cryptopunks.crypton.api.Client
import org.jivesoftware.smack.tcp.XMPPTCPConnection

class IsClientConnected(
    private val connection: XMPPTCPConnection
) : Client.IsConnected {
    override fun invoke(): Boolean = connection.isConnected
}