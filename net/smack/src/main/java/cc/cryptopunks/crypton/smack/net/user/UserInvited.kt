package cc.cryptopunks.crypton.smack.net.user

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate

class UserInvited(
    connection: XMPPTCPConnection
) : User.Net.Invited, (Address) -> Unit by { remoteId ->
    connection.sendStanza(
        Presence(
            JidCreate.from(remoteId.toString()),
            Presence.Type.subscribed
        )
    )
}
