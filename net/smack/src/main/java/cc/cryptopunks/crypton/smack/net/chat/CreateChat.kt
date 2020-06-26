package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.SmackCore
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal fun SmackCore.createChat(
    chat: Chat
) = chat.also {
    require(chat.title.isNotBlank())


    mucManager.getMultiUserChat(JidCreate.entityBareFrom(chat.address.toString())).apply {
        chat.users.filterNot { it.address == chat.account }.forEach { user ->
            invite(JidCreate.entityBareFrom(user.address.id), "")
        }
    }.create(Resourcepart.from(chat.accountUser.address.local)).makeInstant()
}
