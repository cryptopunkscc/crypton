package cc.cryptopunks.crypton.smack.util.ext

import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.chatstates.ChatState
import org.jivesoftware.smackx.chatstates.ChatStateManager
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.element.OmemoElement
import org.jivesoftware.smackx.omemo.element.OmemoElement_VAxolotl

fun <T: Stanza> T.replaceBody(decrypted: OmemoMessage.Received): T =
    when {
        this !is Message -> this
        decrypted.body.isNullOrBlank() -> this
        else -> apply {
            removeOmemoBody()
            body = decrypted.body
        }
    }

fun Stanza.removeOmemoBody() {
    removeExtension(
        Message.Body.ELEMENT,
        Message.Body.NAMESPACE
    )
}

val Stanza.hasOmemoExtension
    get() = hasExtension(
        OmemoElement.NAME_ENCRYPTED,
        OmemoElement_VAxolotl.NAMESPACE
    )

val Stanza.hasChatStateExtension
    get() = hasExtension(
        ChatStateManager.NAMESPACE
    )

val Stanza.chatStateExtension
    get() = ChatState.valueOf(getExtension(ChatStateManager.NAMESPACE).elementName)
