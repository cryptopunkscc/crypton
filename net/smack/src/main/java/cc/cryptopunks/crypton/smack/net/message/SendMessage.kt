package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.isConference
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.entityBareJid
import cc.cryptopunks.crypton.smack.util.entityFullJid
import kotlinx.coroutines.Job
import org.jivesoftware.smack.packet.StanzaBuilder
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun SmackCore.sendMessage(
    message: Message,
    encrypt: Boolean
): Job {
    check(connection.isAuthenticated) { "Connection not authenticated" }

    val stanza = when (message.chat.isConference) {
        true -> prepareConferenceMessage(message, false) // TODO
        false -> prepareChatMessage(message, encrypt)
    }

    return Job().apply {
        connection.addStanzaIdAcknowledgedListener(stanza.setStanzaId()) { complete() }
        connection.sendStanza(stanza)
    }
}

private fun SmackCore.prepareConferenceMessage(message: Message, encrypt: Boolean): SmackMessage =
    when (encrypt) {
        true -> encryptConferenceMessage(message)
        false -> createConferenceMessage(message)
    }

private fun SmackCore.encryptConferenceMessage(message: Message): SmackMessage {
    val toJid = message.to.address.entityBareJid()
    val conference = mucManager.getMultiUserChat(toJid)

    return omemoManager.encrypt(conference, message.text).buildMessage(StanzaBuilder.buildMessage(), toJid)
}

private fun SmackCore.createConferenceMessage(message: Message): SmackMessage =
    mucManager.getMultiUserChat(message.to.address.entityBareJid()).createMessage().apply {
        body = message.text
    }

private fun SmackCore.prepareChatMessage(message: Message, encrypt: Boolean): SmackMessage =
    when (encrypt) {
        true -> encryptChatMessage(message)
        false -> createChatMessage(message)
    }

private fun SmackCore.encryptChatMessage(message: Message): SmackMessage =
    message.to.address.entityBareJid().let { toJid ->
        omemoManager.encrypt(toJid, message.text).buildMessage(StanzaBuilder.buildMessage(), toJid).apply {
            type = org.jivesoftware.smack.packet.Message.Type.chat
        }
    }

private fun createChatMessage(message: Message) =
    SmackMessage().apply {
        to = message.to.entityFullJid()
        body = message.text
        type = SmackMessage.Type.chat
    }
