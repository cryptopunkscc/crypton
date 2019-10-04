package cc.cryptopunks.crypton.smack.user

import cc.cryptopunks.crypton.smack.ApiScope
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.User
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@ApiScope
class UserInvite @Inject constructor(
    connection: XMPPTCPConnection
) : User.Api.Invite, (Address) -> Unit by { remoteId ->
    connection.sendStanza(
        Presence(
            JidCreate.from(remoteId),
            Presence.Type.subscribe
        )
    )
}