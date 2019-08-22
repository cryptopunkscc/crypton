package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.entities.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@ApiScope
class UserInvite @Inject constructor(
    connection: XMPPTCPConnection
) : User.Invite, (User) -> Unit by { user ->
    connection.sendStanza(
        Presence(
            JidCreate.from(user.jid),
            Presence.Type.subscribe
        )
    )
}