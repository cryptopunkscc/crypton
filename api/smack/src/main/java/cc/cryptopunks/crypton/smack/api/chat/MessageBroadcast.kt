package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Message.Api
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.element.OmemoElement
import org.jivesoftware.smackx.omemo.element.OmemoElement_VAxolotl
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jxmpp.jid.Jid
import org.jxmpp.jid.impl.JidCreate
import org.jivesoftware.smack.packet.Message as ApiMessage


internal class MessageBroadcast(
    address: Address,
    private val chatManager: ChatManager,
    private val omemoManager: OmemoManager
) : Api.Broadcast {

    private val userJid = JidCreate.from(address)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Message>) =
        chatMessageFlow(userJid, chatManager, omemoManager)
            .map { it.toCryptonMessage() }
            .collect(collector)

}

private fun chatMessageFlow(
    userJid: Jid,
    chatManager: ChatManager,
    omemoManager: OmemoManager
) = callbackFlow<ApiMessage> {
    val incomingListener = IncomingChatMessageListener { _, message, _: Chat ->
        if (!message.hasExtension(OmemoElement.NAME_ENCRYPTED, OmemoElement_VAxolotl.NAMESPACE))
            channel.offer(message)
    }

    val outgoingListener = OutgoingChatMessageListener { _, message, _ ->
        if (!message.hasExtension(OmemoElement.NAME_ENCRYPTED, OmemoElement_VAxolotl.NAMESPACE))
            channel.offer(message.apply { this.from = userJid })
    }
    val omemoListener = object : OmemoMessageListener {
        override fun onOmemoCarbonCopyReceived(
            direction: CarbonExtension.Direction,
            carbonCopy: ApiMessage,
            wrappingMessage: ApiMessage,
            decryptedCarbonCopy: OmemoMessage.Received
        ) {
            println(decryptedCarbonCopy)
        }

        override fun onOmemoMessageReceived(
            stanza: Stanza,
            decryptedMessage: OmemoMessage.Received
        ) {
            if (stanza !is ApiMessage) return
            stanza.removeExtension(
                org.jivesoftware.smack.packet.Message.Body.ELEMENT,
                org.jivesoftware.smack.packet.Message.Body.NAMESPACE
            )
            stanza.body = decryptedMessage.body
            channel.offer(stanza)
        }
    }


    chatManager.addIncomingListener(incomingListener)
    chatManager.addOutgoingListener(outgoingListener)
    omemoManager.addOmemoMessageListener(omemoListener)

    awaitClose {
        chatManager.removeIncomingListener(incomingListener)
        chatManager.removeOutgoingListener(outgoingListener)
        omemoManager.removeOmemoMessageListener(omemoListener)
    }
}