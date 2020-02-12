package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Message.Net.Send
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import cc.cryptopunks.crypton.util.Broadcast
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import org.jivesoftware.smack.packet.Message as SmackMessage
import org.jivesoftware.smack.roster.Roster
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jxmpp.jid.impl.JidCreate
import java.util.concurrent.Executor
import java.util.concurrent.Executors

internal class SendMessage(
    address: Address,
    private val connection: XMPPTCPConnection,
    private val omemoManager: OmemoManager,
    private val roster: Roster,
    private val outgoingMessageCache: OutgoingMessageCache
) : Send,
    Flow<Message.Net.Event>,
    Executor by Executors.newSingleThreadExecutor() {

    private var lastId = 0

    private val log = typedLog()

    private val fromJid = JidCreate.entityBareFrom(address)

    private val broadcast = Broadcast<Message.Net.Event>()

    override suspend fun invoke(to: Address, text: String) = coroutineScope {
        val id = lastId++
        log.d("$id start in scope: $this")
        val toJid = JidCreate.entityBareFrom(to)

        log.d("$id checking subscription")
        if (!roster.iAmSubscribedTo(toJid) && fromJid != toJid) execute {
            roster.createEntry(toJid, to.local, emptyArray())
            log.d("$id subscribed")
        }

        log.d("$id encrypting")
        val smackMessage = omemoManager
            .encrypt(toJid, text)
            .asMessage(toJid)
            .apply {
                from = fromJid
                type = SmackMessage.Type.chat
                outgoingMessageCache[stanzaId] = text
            }
        val message = smackMessage
            .toCryptonMessage()
            .copy(text = text)

        log.d("$id broadcasting")
        message.run {
            broadcast(Message.Net.Event::Queued)
            registerAckCallback()
        }
        log.d("$id sending")
        if (connection.isAuthenticated) execute {
            log.d("thread start")
            connection.sendStanza(smackMessage)
            log.d("thread stop")
        }
        log.d("$id stop")
        Unit
    }

    private fun Message.registerAckCallback() {
        connection.addStanzaIdAcknowledgedListener(stanzaId) {
            GlobalScope.launch {
                broadcast(Message.Net.Event::Sent)
            }
        }
    }
    private suspend fun Message.broadcast(event: Message.() -> Message.Net.Event) {
        broadcast.run {
            send(event())
            flush()
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Message.Net.Event>) {
        broadcast.collect(collector)
    }
}