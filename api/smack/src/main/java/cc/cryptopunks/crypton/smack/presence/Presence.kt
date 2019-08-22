package cc.cryptopunks.crypton.smack.presence

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.api.entities.Presence
import cc.cryptopunks.crypton.smack.SmackPresence
import org.jivesoftware.smack.XMPPConnection
import javax.inject.Inject

@ApiScope
class SendPresence @Inject constructor(
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