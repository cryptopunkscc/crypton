package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message.Api.Send
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.impl.JidCreate

internal class SendMessage(
    connection: XMPPConnection
) : Send, (Address, String) -> Unit by { to, text ->

    connection.sendStanza(
        Message(
            JidCreate.from(to),
            text
        ).apply {
            from = connection.user
        })
}