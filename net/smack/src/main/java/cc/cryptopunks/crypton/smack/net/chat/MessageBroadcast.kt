package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.CryptonMessage
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.removeOmemoBody
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import cc.cryptopunks.crypton.util.ErrorHandlingScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import org.jivesoftware.smack.packet.Message as SmackMessage


internal class MessageBroadcast(
    address: Address,
    scope: ErrorHandlingScope,
    private val chatManager: ChatManager,
    private val omemoManager: OmemoManager,
    private val encryptedMessageCache: EncryptedMessageCache
) : Message.Net.Broadcast {

    private val userJid = JidCreate.from(address)

    private val channel = BroadcastChannel<CryptonMessage>(Channel.CONFLATED)

    init {
        scope.launch {
            messageFlow(
                userJid = userJid,
                encryptedMessageCache = encryptedMessageCache,
                chatManager = chatManager,
                omemoManager = omemoManager
            ).map { message ->
                message.toCryptonMessage()
            }.collect { message ->
                channel.send(message)
            }
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Message>) =
        channel.asFlow().collect(collector)

}

private fun messageFlow(
    userJid: Jid,
    encryptedMessageCache: EncryptedMessageCache,
    chatManager: ChatManager,
    omemoManager: OmemoManager
): Flow<SmackMessage> =

    callbackFlow {

        val incomingListener = incomingListener()
        val outgoingListener = outgoingListener(userJid, encryptedMessageCache)
        val omemoListener = omemoListener()

        chatManager.addIncomingListener(incomingListener)
        chatManager.addOutgoingListener(outgoingListener)
        omemoManager.addOmemoMessageListener(omemoListener)

        awaitClose {
            chatManager.removeIncomingListener(incomingListener)
            chatManager.removeOutgoingListener(outgoingListener)
            omemoManager.removeOmemoMessageListener(omemoListener)
        }
    }


private fun SendChannel<SmackMessage>.incomingListener() =
    IncomingChatMessageListener { _, message, _: Chat ->
        if (!message.hasOmemoExtension)
            offer(message)
    }


private fun SendChannel<SmackMessage>.outgoingListener(
    userJid: Jid,
    encryptedMessageCache: EncryptedMessageCache
) =
    OutgoingChatMessageListener { _, message, _ ->
        offer(
            message.apply {
                from = userJid
            }.run {
                if (!hasOmemoExtension) message
                else apply {
                    removeOmemoBody()
                    body = encryptedMessageCache[message.stanzaId]
                }
            }
        )
    }


private fun SendChannel<SmackMessage>.omemoListener() = object : OmemoMessageListener {

    override fun onOmemoMessageReceived(
        stanza: Stanza,
        decryptedMessage: OmemoMessage.Received
    ) {
        stanza.let { it as? SmackMessage }
            ?.replaceBody(decryptedMessage)
            ?.let { offer((it)) }
    }

    override fun onOmemoCarbonCopyReceived(
        direction: CarbonExtension.Direction,
        carbonCopy: SmackMessage,
        wrappingMessage: SmackMessage,
        decryptedCarbonCopy: OmemoMessage.Received
    ) = Unit
}