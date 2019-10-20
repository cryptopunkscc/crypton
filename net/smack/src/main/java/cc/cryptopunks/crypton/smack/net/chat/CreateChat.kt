package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.smack.component.SmackComponent
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal class CreateChat(
    component: SmackComponent
) : Chat.Net.Create, (Chat) -> Chat by { chat ->
    component.run {
        mucManager
            .getMultiUserChat(JidCreate.entityBareFrom(chat.address))
            .apply {
                chat.users.dropLast(1).forEach { user ->
                    invite(JidCreate.entityBareFrom(user.address), "")
                }
            }
            .create(Resourcepart.from(chat.accountUser.address.local))
            .makeInstant()
    }
    chat
}