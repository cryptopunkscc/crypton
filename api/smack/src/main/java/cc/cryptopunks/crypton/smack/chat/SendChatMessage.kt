package cc.cryptopunks.crypton.smack.chat

import cc.cryptopunks.crypton.api.ApiScope
import cc.cryptopunks.crypton.entity.Message.Api.Send
import cc.cryptopunks.crypton.entity.RemoteId
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jxmpp.jid.impl.JidCreate
import javax.inject.Inject

@ApiScope
class SendChatMessage @Inject constructor(
    connection: XMPPConnection
) : Send, (RemoteId, String) -> Unit by { to, text ->

    connection.sendStanza(
        Message(
            JidCreate.from(to),
            text
        ).apply {
            from = connection.user
        })
}