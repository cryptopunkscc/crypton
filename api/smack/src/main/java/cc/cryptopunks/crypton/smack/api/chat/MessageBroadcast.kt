package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.CryptonMessage
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.removeOmemoBody
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
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
import org.jxmpp.jid.impl.JidCreate
import org.jivesoftware.smack.packet.Message as ApiMessage


internal class MessageBroadcast(
    address: Address,
    scope: Api.Scope,
    private val chatManager: ChatManager,
    private val omemoManager: OmemoManager,
    private val encryptedMessageCache: EncryptedMessageCache
) : Message.Api.Broadcast {

    private val userJid = JidCreate.from(address)

    private val channel = BroadcastChannel<CryptonMessage>(Channel.CONFLATED)

    init {
        scope.launch {
            messageFlow().collect {
                channel.send(it)
            }
        }
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Message>) =
        channel.asFlow().collect(collector)


    private fun messageFlow() = callbackFlow<ApiMessage> {
        val incomingListener = IncomingChatMessageListener { _, message, _: Chat ->
            if (!message.hasOmemoExtension)
                channel.offer(message)
        }

        val outgoingListener = OutgoingChatMessageListener { _, message, _ ->
            channel.offer(
                message.run {
                    if (!hasOmemoExtension) message
                    else apply {
                        removeOmemoBody()
                        body = encryptedMessageCache[message.stanzaId]
                    }
                }.apply {
                    from = userJid
                }
            )
        }

        val omemoListener = object : OmemoMessageListener {

            override fun onOmemoMessageReceived(
                stanza: Stanza,
                decryptedMessage: OmemoMessage.Received
            ) {
                stanza.let { it as? ApiMessage }
                    ?.replaceBody(decryptedMessage)
                    ?.let(channel::offer)
            }

            override fun onOmemoCarbonCopyReceived(
                direction: CarbonExtension.Direction,
                carbonCopy: ApiMessage,
                wrappingMessage: ApiMessage,
                decryptedCarbonCopy: OmemoMessage.Received
            ) = Unit
        }

        chatManager.addIncomingListener(incomingListener)
        chatManager.addOutgoingListener(outgoingListener)
        omemoManager.addOmemoMessageListener(omemoListener)

        awaitClose {
            chatManager.removeIncomingListener(incomingListener)
            chatManager.removeOutgoingListener(outgoingListener)
            omemoManager.removeOmemoMessageListener(omemoListener)
        }
    }.map { message ->
        message.toCryptonMessage()
    }
}