package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.CryptonMessage
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import cc.cryptopunks.crypton.util.all
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.StanzaListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.filter.StanzaFilter
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.carbons.packet.CarbonExtension
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.listener.OmemoMessageListener
import org.jivesoftware.smackx.omemo.listener.OmemoMucMessageListener
import org.jivesoftware.smack.packet.Message as SmackMessage

internal fun SmackCore.createMessageEventBroadcast(): BroadcastChannel<CryptonMessage> =
    callbackFlow<CryptonMessage> {

        val incomingMucListener = incomingMucListener(connection)
        val omemoListener = omemoListener()

        omemoManager.addOmemoMessageListener(omemoListener)
        omemoManager.addOmemoMucMessageListener(omemoListener)
        connection.addAsyncStanzaListener(incomingMucListener, StanzaFilter { true })

        awaitClose {
            omemoManager.removeOmemoMessageListener(omemoListener)
            omemoManager.removeOmemoMucMessageListener(omemoListener)
            connection.removeAsyncStanzaListener(incomingMucListener)
        }
    }
        .broadcastIn(this)


private fun SendChannel<CryptonMessage>.incomingMucListener(connection: XMPPConnection) =
    StanzaListener { stanza ->
        (stanza as? SmackMessage)?.takeIf {
            when {
                it.hasOmemoExtension -> false

                it.type == SmackMessage.Type.chat -> true

                all( // filter own messages
                    SmackMessage.Type.groupchat == it.type,
                    connection.user.localpart != it.from.resourceOrEmpty
                ) -> true

                else -> false
            }
        }?.run {
            offer(toCryptonMessage(status = Message.Status.Received).run {
                copy(
                    chat = when (type) {
                        SmackMessage.Type.chat -> from.address
                        SmackMessage.Type.groupchat -> to.address
                        else -> throw IllegalArgumentException("invalid message type $type")
                    }
                )
            })
        }
    }


private fun SendChannel<CryptonMessage>.omemoListener() = object :
    OmemoMessageListener,
    OmemoMucMessageListener {

    override fun onOmemoMessageReceived(
        stanza: Stanza,
        decryptedMessage: OmemoMessage.Received
    ) {
        stanza.let {
            it as? SmackMessage
        }?.replaceBody(decryptedMessage)?.run {
            offer(toCryptonMessage(Message.Status.Received).run {
                copy(chat = from.address)
            })
        }
    }

    override fun onOmemoCarbonCopyReceived(
        direction: CarbonExtension.Direction,
        carbonCopy: SmackMessage,
        wrappingMessage: SmackMessage,
        decryptedCarbonCopy: OmemoMessage.Received
    ) {
        carbonCopy.replaceBody(decryptedCarbonCopy)?.run {
            offer(toCryptonMessage(Message.Status.Sent).run {
                copy(chat = to.address)
            })
        }
    }

    override fun onOmemoMucMessageReceived(
        muc: MultiUserChat,
        stanza: Stanza,
        decryptedOmemoMessage: OmemoMessage.Received
    ) {
        stanza.let {
            it as? SmackMessage
        }?.replaceBody(decryptedOmemoMessage)?.run {
            offer(toCryptonMessage(Message.Status.Received).run {
                copy(chat = from.address)
            })
        }
    }
}
