package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.util.TypedLog
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jxmpp.jid.impl.JidCreate

private class SendMessage

private var lastId = 0

internal fun createSendMessage(
    address: Address,
    connection: XMPPTCPConnection,
    omemoManager: OmemoManager,
    roster: Roster,
    broadcast: SendChannel<Message.Net.Event>,
    log: TypedLog = SendMessage().typedLog()
): suspend (Message) -> Unit = { message ->
    if (!connection.isAuthenticated) throw Exception("Connection not authenticated")
    val id = lastId++

    log.d("$id start sending")
    val fromJid = JidCreate.entityBareFrom(address.toString())
    val toJid = JidCreate.entityBareFrom(message.to.address.toString())

    log.d("$id checking subscription")
    if (!roster.iAmSubscribedTo(toJid) && fromJid != toJid) {
        roster.createEntry(toJid, message.to.address.local, emptyArray())
        log.d("$id subscribed")
    } else {
        log.d("$id encrypting")
        val smackMessage = omemoManager
            .encrypt(toJid, message.text)
            .asMessage(toJid)
            .apply {
                from = fromJid
                type = org.jivesoftware.smack.packet.Message.Type.chat
            }

        connection.addStanzaIdAcknowledgedListener(smackMessage.stanzaId) {
            GlobalScope.launch { broadcast.send(Message.Net.Event.Sent(message)) }
        }

        log.d("$id sending")
        broadcast.send(Message.Net.Event.Sending(message))
        connection.sendStanza(smackMessage)
        log.d("$id send")
        log.d("$id stop")
    }
}

