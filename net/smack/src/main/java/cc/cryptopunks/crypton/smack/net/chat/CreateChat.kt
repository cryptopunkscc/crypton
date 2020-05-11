package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.core.SmackCore
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal class CreateChat(
    core: SmackCore
) : Chat.Net.Create, (Chat) -> Chat by { chat ->
    core.run {
        mucManager
            .getMultiUserChat(JidCreate.entityBareFrom(chat.address.toString()))
            .apply {
                chat.users.dropLast(1).forEach { user ->
                    invite(JidCreate.entityBareFrom(user.address.toString()), "")
                }
            }
            .create(Resourcepart.from(chat.accountUser.address.local))
            .makeInstant()
    }
    chat
}
