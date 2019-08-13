package cc.cryptopunks.crypton.smack.chat

import cc.cryptopunks.crypton.xmpp.XmppScope
import cc.cryptopunks.crypton.xmpp.entities.ChatMessage
import cc.cryptopunks.crypton.xmpp.entities.Jid
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@XmppScope
class ChatMessageSend @Inject constructor(
    connection: XMPPConnection
) : ChatMessage.Send, (Jid, String) -> Unit by { to, text ->

    connection.sendStanza(
        Message(
            JidCreate.from(to),
            text
        ).apply {
            from = connection.user
        })
}