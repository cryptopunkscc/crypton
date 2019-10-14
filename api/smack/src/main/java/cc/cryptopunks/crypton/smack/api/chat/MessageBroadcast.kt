package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener
import org.jxmpp.jid.impl.JidCreate


internal class MessageBroadcast(
    chatManager: ChatManager,
    address: Address
) : Message.Api.Broadcast,
    Flow<Message> by chatManager.chatMessageFlow(address)

private fun ChatManager.chatMessageFlow(
    address: Address
): Flow<Message> = callbackFlow {
    val incomingListener = IncomingChatMessageListener { _, message, chat: Chat ->
        channel.offer(
            message.toCryptonMessage()
        )
    }

    val outgoingListener = OutgoingChatMessageListener { _, message, _ ->
        channel.offer(
            message.apply {
                from = JidCreate.from(address)
            }.toCryptonMessage()
        )
    }

    addIncomingListener(incomingListener)
    addOutgoingListener(outgoingListener)

    awaitClose {
        removeIncomingListener(incomingListener)
        removeOutgoingListener(outgoingListener)
    }
}