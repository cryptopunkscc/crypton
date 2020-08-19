package cc.cryptopunks.crypton.smack.util

import cc.cryptopunks.crypton.context.*
import org.jivesoftware.smackx.delay.packet.DelayInformation
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.sid.element.StanzaIdElement
import org.jxmpp.jid.impl.JidCreate

internal fun Forwarded.cryptonMessage(): CryptonMessage =
    (forwardedStanza as SmackMessage).cryptonMessage(timestamp = delayInformation.stamp.time)

internal fun SmackMessage.cryptonMessage(
    status: Message.Status = Message.Status.None,
    id: String = stanzaElementId(),
    timestamp: Long = timestamp() ?: System.currentTimeMillis(),
    from: Resource = this.from.resource(),
    to: Resource = this.to.resource(),
    decrypted: OmemoMessage.Received? = null,
    body: String = decrypted?.body ?: this.body ?: "",
    chat: Address = when (status) {
        Message.Status.State ->
            if (to.address.isConference) to.address
            else from.address
        Message.Status.Received -> from.address
        Message.Status.Sent -> to.address
        else -> Address.Empty
    },
    encrypted: Boolean = true
) = CryptonMessage(
    id = id,
    text = body,
    from = from,
    to = to,
    chat = chat,
    timestamp = timestamp,
    stanzaId = id,
    status = status,
    encrypted = encrypted
)

internal fun SmackMessage.timestamp() = delay()?.stamp?.time

internal fun SmackMessage.delay() =
    extensions.filterIsInstance<DelayInformation>().firstOrNull()

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
