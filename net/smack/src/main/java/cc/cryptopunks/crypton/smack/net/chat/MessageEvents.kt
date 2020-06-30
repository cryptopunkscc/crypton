package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smackx.omemo.listener.OmemoMucMessageListener
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun createMessageEventBroadcast(
    scope: CoroutineScope,
    chatManager: ChatManager,
    omemoManager: OmemoManager
): BroadcastChannel<Message.Net.Event> = callbackFlow<MessageEvent> {

    val incomingListener = incomingListener()
    val omemoListener = omemoListener()

    chatManager.addIncomingListener(incomingListener)
    omemoManager.addOmemoMessageListener(omemoListener)
    omemoManager.addOmemoMucMessageListener(omemoListener)

    awaitClose {
        chatManager.removeIncomingListener(incomingListener)
        omemoManager.removeOmemoMessageListener(omemoListener)
        omemoManager.removeOmemoMucMessageListener(omemoListener)
    }
}
    .mapNotNull { it.cryptonMessage() }
    .map { Message.Net.Event(it) }
    .broadcastIn(scope)


private fun MessageEvent.cryptonMessage(): Message? = if (
    setOf(
        SmackMessage.Type.chat,
        SmackMessage.Type.groupchat
    ).contains(first.type).not()
) null else when (second) {
    MessageType.Incoming -> Message.Status.Received
    MessageType.CarbonCopy -> Message.Status.Sent
    MessageType.Outgoing -> null
}?.let { status ->
    first.toCryptonMessage().copy(
        status = status,
        chatAddress = when(first.type) {
            SmackMessage.Type.chat -> first.from.address()
            SmackMessage.Type.groupchat -> first.to.address()
            else -> Address.Empty
        }
    )
}


private fun SendChannel<MessageEvent>.incomingListener() =
    IncomingChatMessageListener { _, message, _: Chat ->
        if (!message.hasOmemoExtension)
            offer(message to MessageType.Incoming)
    }


private fun SendChannel<MessageEvent>.omemoListener() = object :
    OmemoMessageListener,
    OmemoMucMessageListener {

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

    override fun onOmemoMucMessageReceived(
        muc: MultiUserChat,
        stanza: Stanza,
        decryptedOmemoMessage: OmemoMessage.Received
    ) {
        onOmemoMessageReceived(stanza, decryptedOmemoMessage)
    }
}
