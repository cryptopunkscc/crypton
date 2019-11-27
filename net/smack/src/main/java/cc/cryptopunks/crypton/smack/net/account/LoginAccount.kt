package cc.cryptopunks.crypton.smack.net.account

import cc.cryptopunks.crypton.context.Account
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.carbons.CarbonManager

class LoginAccount(
    connection: XMPPTCPConnection,
    carbonManager: CarbonManager
) : Account.Net.Login, () -> Unit by {
    connection.login()
    carbonManager.enableCarbons()
}