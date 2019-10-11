package cc.cryptopunks.crypton.smack.api.user

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate

class UserInvite(
    connection: XMPPTCPConnection
) : User.Api.Invite, (Address) -> Unit by { remoteId ->
    connection.sendStanza(
        Presence(
            JidCreate.from(remoteId),
            Presence.Type.subscribe
        )
    )
}