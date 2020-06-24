package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.Job
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jxmpp.jid.impl.JidCreate

internal fun createSendMessage(
    address: Address,
    connection: XMPPTCPConnection,
    omemoManager: OmemoManager
): suspend (Message) -> Job = { message ->
    if (!connection.isAuthenticated) throw Exception("Connection not authenticated")

    val fromJid = JidCreate.entityBareFrom(address.toString())
    val toJid = JidCreate.entityBareFrom(message.to.address.toString())

    val smackMessage = omemoManager.encrypt(toJid, message.text).asMessage(toJid).apply {
        from = fromJid
        type = org.jivesoftware.smack.packet.Message.Type.chat
    }

    Job().apply {
        connection.addStanzaIdAcknowledgedListener(smackMessage.stanzaId) { complete() }
        connection.sendStanza(smackMessage)
    }
}
