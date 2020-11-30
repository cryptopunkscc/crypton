package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.isConference
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.entityBareJid
import kotlinx.coroutines.Job
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun SmackCore.sendMessage(message: Message): Job {
    check(connection.isAuthenticated) { "Connection not authenticated" }

    val stanza = prepareStanza(message)

    return Job().apply {
        connection.addStanzaIdAcknowledgedListener(stanza.setStanzaId()) { complete() }
        connection.sendStanza(stanza)
    }
}

private fun SmackCore.prepareStanza(message: Message) =
    when (message.chat.isConference) {
        true -> prepareConferenceMessage(message)
        false -> prepareChatMessage(message)
    }

private fun SmackCore.prepareConferenceMessage(message: Message): SmackMessage =
    when (message.encrypted) {
        true -> encryptConferenceMessage(message)
        false -> createConferenceMessage(message)
    }

private fun SmackCore.encryptConferenceMessage(message: Message): SmackMessage {
    val toJid = message.to.address.entityBareJid()
    val conference = mucManager.getMultiUserChat(toJid)

    return omemoManager.encrypt(conference, message.body).asMessage(toJid).apply {
        type = org.jivesoftware.smack.packet.Message.Type.groupchat
    }
}

private fun SmackCore.createConferenceMessage(message: Message): SmackMessage =
    mucManager.getMultiUserChat(message.to.address.entityBareJid()).createMessage().apply {
        body = message.body
    }

private fun SmackCore.prepareChatMessage(message: Message): SmackMessage =
    when (message.encrypted) {
        true -> encryptChatMessage(message)
        false -> createChatMessage(message)
    }

private fun SmackCore.encryptChatMessage(message: Message): SmackMessage =
    message.to.address.entityBareJid().let { toJid ->
        omemoManager.encrypt(toJid, message.body).asMessage(toJid).apply {
            type = org.jivesoftware.smack.packet.Message.Type.chat
        }
    }

private fun createChatMessage(message: Message) =
    SmackMessage().apply {
        to = message.to.address.entityBareJid()
        body = message.body
        type = SmackMessage.Type.chat
    }
