package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.bareJid
import kotlinx.coroutines.Job
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun createSendMessage(
    connection: XMPPTCPConnection,
    encrypt: (Message) -> SmackMessage
): suspend (Message) -> Job = { message ->
    if (!connection.isAuthenticated) throw Exception("Connection not authenticated")

    val smackMessage = encrypt(message).apply {
        from = message.from.address.bareJid()
        type = org.jivesoftware.smack.packet.Message.Type.chat
    }

    Job().apply {
        connection.addStanzaIdAcknowledgedListener(smackMessage.stanzaId) { complete() }
        connection.sendStanza(smackMessage)
    }
}

internal fun SmackCore.encryptSingle(): (Message) -> SmackMessage = { message: Message ->
    val toJid = message.to.address.bareJid()

    omemoManager.encrypt(toJid, message.text).asMessage(toJid)
}

internal fun SmackCore.encryptMulti() = { message: Message ->
    val toJid = message.to.address.bareJid()
    val chat = mucManager.getMultiUserChat(toJid)

    omemoManager.encrypt(chat, message.text).asMessage(toJid)
}
