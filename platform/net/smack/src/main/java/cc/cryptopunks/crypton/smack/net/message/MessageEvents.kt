package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.CryptonMessage
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.cryptonMessage
import cc.cryptopunks.crypton.smack.util.ext.chatStateExtension
import cc.cryptopunks.crypton.smack.util.ext.hasChatStateExtension
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.util.all
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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

        val stanzaListener = stanzaListener(connection)
        val omemoListener = omemoListener()

        omemoManager.addOmemoMessageListener(omemoListener)
        omemoManager.addOmemoMucMessageListener(omemoListener)
        connection.addAsyncStanzaListener(stanzaListener, StanzaFilter { true })

        awaitClose {
            omemoManager.removeOmemoMessageListener(omemoListener)
            omemoManager.removeOmemoMucMessageListener(omemoListener)
            connection.removeAsyncStanzaListener(stanzaListener)
        }
    }
        .map { it.calculateId() }
        .broadcastIn(this)


private fun SendChannel<CryptonMessage>.stanzaListener(connection: XMPPConnection) =
    StanzaListener { stanza ->
//        println("incoming stanza")
        when {
            stanza.hasOmemoExtension -> Unit
            stanza is SmackMessage -> when {

                SmackMessage.Type.chat == stanza.type -> {
                    stanza
                }

                all( // filter own messages
                    SmackMessage.Type.groupchat == stanza.type,
                    connection.user.localpart != stanza.from.resourceOrEmpty
                ) -> stanza

                else -> {
//                    println("Unknown stanza received") TODO
                    null
                }
            }?.run {
                when {
                    stanza.hasChatStateExtension -> cryptonMessage(
                        status = Message.Status.State,
                        text = stanza.chatStateExtension.name
                    )
                    stanza.body.isNullOrEmpty() -> null
                    else -> cryptonMessage(Message.Status.Received)
                }?.also {
//                    println("offering message $it") TODO
                    offer(it.copy(encrypted = false))
                }
//                    ?: println("Unknown stanza with empty body received") TODO
            }

            else -> {
//                println("Unknown stanza received") TODO
            }
        }
    }


private fun SendChannel<CryptonMessage>.omemoListener() = object :
    OmemoMessageListener,
    OmemoMucMessageListener {

    override fun onOmemoMessageReceived(
        stanza: Stanza,
        decryptedMessage: OmemoMessage.Received
    ) {
//        println("Omemo omemo stanza received")
        when {
            decryptedMessage.body.isNullOrBlank() -> {
//                println("Null or blank body")
            }
            stanza is SmackMessage -> stanza.cryptonMessage(
                status = Message.Status.Received,
                decrypted = decryptedMessage
            ).also {
//                println("offering omemo message $it")
                offer(it)
            }
            else -> {
//                println("Unknown omemo stanza received")
            }
        }
    }

    override fun onOmemoCarbonCopyReceived(
        direction: CarbonExtension.Direction,
        carbonCopy: SmackMessage,
        wrappingMessage: SmackMessage,
        decryptedCarbonCopy: OmemoMessage.Received
    ) {
//        println("Omemo omemo carbon copy received")
        carbonCopy.cryptonMessage(
            status = Message.Status.Sent,
            decrypted = decryptedCarbonCopy
        ).also {
//            println("offering carbon copy $it")
            offer(it)
        }
    }

    override fun onOmemoMucMessageReceived(
        muc: MultiUserChat,
        stanza: Stanza,
        decryptedOmemoMessage: OmemoMessage.Received
    ) {
//        println("Omemo muc stanza received")
        when (stanza) {
            is SmackMessage -> stanza.cryptonMessage(
                status = Message.Status.Received,
                decrypted = decryptedOmemoMessage
            ).also {
//                println("offering omemo muc message $it")
                offer(it)
            }
            else -> {
//                println("Unknown muc omemo stanza received")
            }
        }
    }
}
