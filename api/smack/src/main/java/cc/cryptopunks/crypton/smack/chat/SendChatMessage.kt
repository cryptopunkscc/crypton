package cc.cryptopunks.crypton.smack.chat

import cc.cryptopunks.crypton.smack.ApiScope
import cc.cryptopunks.crypton.entity.Message.Api.Send
import cc.cryptopunks.crypton.entity.Address
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@ApiScope
internal class SendChatMessage @Inject constructor(
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