package cc.cryptopunks.crypton.smack.presence

import cc.cryptopunks.crypton.xmpp.XmppScope
import cc.cryptopunks.crypton.xmpp.entities.Presence
import cc.cryptopunks.crypton.smack.SmackPresence
import org.jivesoftware.smack.XMPPConnection
import javax.inject.Inject

@XmppScope
class PresenceSend @Inject constructor(
    connection: XMPPConnection
) : Presence.Send, (Presence) -> Unit by { presence ->
    connection.sendStanza(
        SmackPresence(
            org.jivesoftware.smack.packet.Presence.Type.fromString(
                presence.status.name.toLowerCase()
            )
        )
    )
}