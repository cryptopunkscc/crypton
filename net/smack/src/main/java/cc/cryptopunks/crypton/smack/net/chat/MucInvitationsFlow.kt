package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.bareJid
import cc.cryptopunks.crypton.smack.util.resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.muc.InvitationListener
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.muc.packet.MUCUser
import org.jxmpp.jid.EntityJid
import org.jxmpp.jid.parts.Resourcepart


fun MultiUserChatManager.invitationsFlow() =
    callbackFlow<Chat.Net.MucInvitation> {
        InvitationListener { conn: XMPPConnection,
                             room: MultiUserChat,
                             inviter: EntityJid,
                             reason: String?,
                             password: String?,
                             message: Message,
                             invitation: MUCUser.Invite ->
            offer(
                Chat.Net.MucInvitation(
                    address = room.room.address(),
                    inviter = inviter.resource(),
                    password = password,
                    reason = reason
                ).also {
                    println("new invitation $it")
                }
            )
        }.let { listener ->
            addInvitationListener(listener)
            awaitClose {
                removeInvitationListener(listener)
            }
        }
    }


fun MultiUserChatManager.join(chat: Address, nickname: String) {
    getMultiUserChat(chat.bareJid()).join(Resourcepart.from(nickname))
}
