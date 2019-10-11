package cc.cryptopunks.crypton.smack.api.user

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate

class UserInvited(
    connection: XMPPTCPConnection
) : User.Api.Invited, (Address) -> Unit by { remoteId ->
    connection.sendStanza(
        Presence(
            JidCreate.from(remoteId),
            Presence.Type.subscribed
        )
    )
}