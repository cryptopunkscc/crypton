package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.context.*
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.sid.element.StanzaIdElement
import org.jxmpp.jid.impl.JidCreate

internal fun Forwarded.toCryptonMessage() = (forwardedStanza as Message)
    .toCryptonMessage(delayInformation.stamp.time)

internal fun Message.toCryptonMessage(
    timestamp: Long = System.currentTimeMillis()
) = CryptonMessage(
    id = stanzaElementId(),
    text = body ?: "",
    from = from.resource(),
    to = to.resource(),
    timestamp = timestamp,
    stanzaId = stanzaElementId()
)

internal fun Message.stanzaElementId() = extensions.filterIsInstance<StanzaIdElement>().firstOrNull()?.id ?: stanzaId ?: ""

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

internal fun Address.bareJid() = JidCreate.entityBareFrom(toString())


internal fun MultiUserChat.toChat(account: Address) = Chat(
    title = room.toString(),
    account = account,
    address = room.resource().address,
    resource = room.resource(),
    users = this.members.map { User(it.jid.resource().address) }
)
