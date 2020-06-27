package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.util.bareJid
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal fun MultiUserChatManager.createMuc(
    chat: Chat
) = chat.also {
    require(chat.title.isNotBlank())

    getMultiUserChat(JidCreate.entityBareFrom(chat.address.toString())).apply {
        // create reserved muc
        create(Resourcepart.from(chat.accountUser.address.local))
            .configFormManager
            .setRoomOwners(listOf(chat.account.bareJid()))
            .submitConfigurationForm()
        // invite users
        chat.users.filterNot { it.address == chat.account }.forEach { user ->
            invite(JidCreate.entityBareFrom(user.address.id), "")
        }
    }
}
