package cc.cryptopunks.crypton.smack.util.ext

import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smackx.omemo.OmemoMessage
import org.jivesoftware.smackx.omemo.element.OmemoElement
import org.jivesoftware.smackx.omemo.element.OmemoElement_VAxolotl

fun <T: Stanza> T.replaceBody(decrypted: OmemoMessage.Received): T? =
    if (this !is Message || decrypted.body.isNullOrBlank()) null
    else apply {
        removeOmemoBody()
        body = decrypted.body
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