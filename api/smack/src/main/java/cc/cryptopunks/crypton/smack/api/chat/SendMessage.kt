package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message.Api.Send
import cc.cryptopunks.crypton.smack.component.SmackComponent
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.impl.JidCreate

internal class SendMessage(
    smack: SmackComponent
) : Send, (Address, String) -> Unit by { to, text ->
    smack.run {
        connection.sendStanza(
            Message(
                JidCreate.from(to),
                text
            ).apply {
                from = connection.user
            })
    }
}