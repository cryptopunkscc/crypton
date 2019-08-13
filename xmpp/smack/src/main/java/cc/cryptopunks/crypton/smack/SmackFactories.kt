package cc.cryptopunks.crypton.smack

import cc.cryptopunks.crypton.xmpp.entities.ChatMessage
import cc.cryptopunks.crypton.xmpp.entities.Jid
import cc.cryptopunks.crypton.xmpp.entities.Presence
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.delay.packet.DelayInformation
import org.jivesoftware.smackx.sid.element.StanzaIdElement
import org.jxmpp.jid.impl.JidCreate
import java.util.*


internal fun chatMessage(
    message: Message = Message(),
    delayInformation: DelayInformation = DelayInformation(Date())
) = ChatMessage(
    id = message.extensions.filterIsInstance<StanzaIdElement>().firstOrNull()?.id ?: "",
    body = message.body,
    from = message.from.jid(),
    to = message.to.jid(),
    stamp = delayInformation.stamp,
    stanzaId = message.stanzaId
)


internal fun SmackJid.jid() = Jid(
    local = localpartOrNull?.toString() ?: "",
    domain = domain.toString(),
    resource = resourceOrNull?.toString() ?: ""
)

internal fun String.jid(): Jid = JidCreate.from(toString()).jid()

internal fun SmackPresence.presence() = Presence(
    status = Presence.Status.values()[type.ordinal]
)