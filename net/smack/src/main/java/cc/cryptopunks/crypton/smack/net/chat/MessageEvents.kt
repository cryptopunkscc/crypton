package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun createMessageEventBroadcast(
    scope: CoroutineScope,
    chatManager: ChatManager,
    omemoManager: OmemoManager
): BroadcastChannel<Message.Net.Event> =
    messageFlow(
        chatManager = chatManager,
        omemoManager = omemoManager
    )
        .mapNotNull { it.cryptonMessage() }
        .map { Message.Net.Event(it) }
        .broadcastIn(scope)

private fun messageFlow(
    chatManager: ChatManager,
    omemoManager: OmemoManager
): Flow<MessageEvent> = callbackFlow {

    val incomingListener = incomingListener()
    val omemoListener = omemoListener()

    chatManager.addIncomingListener(incomingListener)
    omemoManager.addOmemoMessageListener(omemoListener)

    awaitClose {
        chatManager.removeIncomingListener(incomingListener)
        omemoManager.removeOmemoMessageListener(omemoListener)
    }
}

private fun MessageEvent.cryptonMessage(): Message? =
    if (first.type != SmackMessage.Type.chat) null
    else when (second) {
        MessageType.Incoming -> Message.Status.Received
        MessageType.CarbonCopy -> Message.Status.Sent
        MessageType.Outgoing -> null
    }?.let { status ->
        first.toCryptonMessage().copy(status = status)
    }

private fun SendChannel<MessageEvent>.incomingListener() =
    IncomingChatMessageListener { _, message, _: Chat ->
        if (!message.hasOmemoExtension)
            offer(message to MessageType.Incoming)
    }


private fun SendChannel<MessageEvent>.omemoListener() = object : OmemoMessageListener {

    override fun onOmemoMessageReceived(
        stanza: Stanza,
        decryptedMessage: OmemoMessage.Received
    ) {
        stanza.let { it as? SmackMessage }
            ?.replaceBody(decryptedMessage)
            ?.let { offer(it to MessageType.Incoming) }
    }

    override fun onOmemoCarbonCopyReceived(
        direction: CarbonExtension.Direction,
        carbonCopy: SmackMessage,
        wrappingMessage: SmackMessage,
        decryptedCarbonCopy: OmemoMessage.Received
    ) {
        carbonCopy
            .replaceBody(decryptedCarbonCopy)
            ?.let { offer(it to MessageType.CarbonCopy) }
    }
}
