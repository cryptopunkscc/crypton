package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.context.*
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.sid.element.StanzaIdElement
import org.jxmpp.jid.impl.JidCreate

internal fun Forwarded.toCryptonMessage() = (forwardedStanza as SmackMessage)
    .toCryptonMessage(timestamp = delayInformation.stamp.time)

internal fun SmackMessage.toCryptonMessage(
    status: Message.Status = Message.Status.None,
    timestamp: Long = System.currentTimeMillis(),
    chat: Address = Address.Empty
) = CryptonMessage(
    id = stanzaElementId(),
    text = body ?: "",
    from = from.resource(),
    to = to.resource(),
    chat = chat,
    timestamp = timestamp,
    stanzaId = stanzaElementId(),
    status = status
)

internal fun SmackMessage.stanzaElementId() =
    extensions.filterIsInstance<StanzaIdElement>().firstOrNull()?.id ?: stanzaId ?: ""

internal fun String.resource(): Resource = JidCreate.from(toString()).resource()

internal fun String.remoteId() = JidCreate.from(toString()).address()

internal fun SmackJid.resource() = Resource(
    address = address(),
    resource = resourceOrNull?.toString() ?: ""
)

internal fun SmackJid.address() = Address(
    local = localpartOrNull?.toString() ?: "",
    domain = domain.toString()
)

internal fun SmackPresence.presence(address: SmackJid?) = Presence(
    resource = address?.resource() ?: Resource.Empty,
    status = Presence.Status.values().first { it.name.toLowerCase() == type.name }
)

internal fun Address.entityBareJid() = JidCreate.entityBareFrom(toString())
internal fun Resource.entityFullJid() = JidCreate.entityFullFrom(toString())


internal fun MultiUserChat.toChat(account: Address) = Chat(
    account = account,
    address = room.resource().address,
    users = this.members.map { it.jid.resource().address }
)
