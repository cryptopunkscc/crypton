package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.net.chat.Muc.RoomConfig.ALLOW_INVITES
import cc.cryptopunks.crypton.smack.net.chat.Muc.RoomConfig.MEMBERS_ONLY
import cc.cryptopunks.crypton.smack.net.chat.Muc.RoomConfig.PERSISTENT_ROOM
import cc.cryptopunks.crypton.smack.net.chat.Muc.RoomConfig.PUBLIC_ROOM
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal fun MultiUserChatManager.createMuc(
    chat: Chat
) = chat.also {
    require(chat.title.isNotBlank())
    println("Creating muc $chat")
    getMultiUserChat(JidCreate.entityBareFrom(chat.address.toString())).apply {
        // create reserved muc
        createOrJoin(Resourcepart.from(chat.account.local))

        // configure
        configurationForm.fields.forEachIndexed { index, formField ->
            println(formField.toXML("$index"))
        }
        sendConfigurationForm(
            configurationForm.createAnswerForm().apply {
                setAnswer(MEMBERS_ONLY, true)
                setAnswer(PUBLIC_ROOM, false)
                setAnswer(ALLOW_INVITES, true)
                setAnswer(PERSISTENT_ROOM, true)
            }
        )

        // invite users
        chat.users.filterNot { it.address == chat.account }.forEach { user ->
            println("Muc inviting $user")
            invite(JidCreate.entityBareFrom(user.address.id), "")
        }
    }
}
