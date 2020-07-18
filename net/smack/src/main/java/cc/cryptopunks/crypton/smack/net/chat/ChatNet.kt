package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.address
import cc.cryptopunks.crypton.smack.util.entityBareJid
import cc.cryptopunks.crypton.smack.util.resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.muc.InvitationListener
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.muc.packet.MUCUser
import org.jxmpp.jid.EntityJid
import org.jxmpp.jid.impl.JidCreate
import org.jxmpp.jid.parts.Resourcepart

internal class ChatNet(
    private val smackCore: SmackCore
) :
    SmackCore by smackCore,
    Chat.Net {

    override fun supportEncryption(address: Address): Boolean =
        omemoManager.multiUserChatSupportsOmemo(mucManager.getMultiUserChat(address.entityBareJid()))


    override fun createConference(chat: Chat): Chat = smackCore.createMuc(chat)

    override fun inviteToConference(chat: Address, users: List<Address>) =
        smackCore.inviteToConference(chat, users)

    override fun conferenceInvitationsFlow(): Flow<Chat.Net.ConferenceInvitation> =
        mucManager.invitationsFlow()

    override fun joinConference(address: Address, nickname: String) =
        mucManager.join(address, nickname)

    override fun listJoinedRooms(): Set<Address> =
        mucManager.joinedRooms.map { it.address() }.toSet()

    override fun listRooms(): Set<Address> =
        mucManager.mucServiceDomains.map(mucManager::getHostedRooms).flatten()
            .map { it.jid.address() }.toSet()
}

internal fun SmackCore.inviteToConference(
    chat: Address,
    users: List<Address>
) {
    mucManager.getMultiUserChat(chat.entityBareJid()).apply {
        users.forEach { user ->
            invite(user.entityBareJid(), "no reason")
        }
    }
}

internal fun SmackCore.createMuc(
    chat: Chat
) = chat.also {
    println("Creating muc $chat")
    mucManager.getMultiUserChat(chat.address.entityBareJid()).apply {

        // create reserved muc
        createOrJoin(Resourcepart.from(chat.account.local))

        // configure
        configurationForm.fields.forEachIndexed { index, formField ->
            println(formField.toXML("$index"))
        }
        sendConfigurationForm(
            configurationForm.createAnswerForm().apply {
                setAnswer(Muc.RoomConfig.MEMBERS_ONLY, true)
                setAnswer(Muc.RoomConfig.PUBLIC_ROOM, false)
                setAnswer(Muc.RoomConfig.ALLOW_INVITES, true)
                setAnswer(Muc.RoomConfig.PERSISTENT_ROOM, true)
            }
        )

        // invite users
        chat.users.filterNot { it == chat.account }.forEach { user ->
            println("Muc inviting $user")
            invite(JidCreate.entityBareFrom(user.id), "")
        }
    }
}

fun MultiUserChatManager.invitationsFlow() =
    callbackFlow<Chat.Net.ConferenceInvitation> {
        InvitationListener { conn: XMPPConnection,
                             room: MultiUserChat,
                             inviter: EntityJid,
                             reason: String?,
                             password: String?,
                             message: Message,
                             invitation: MUCUser.Invite ->
            offer(
                Chat.Net.ConferenceInvitation(
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
    getMultiUserChat(chat.entityBareJid()).join(Resourcepart.from(nickname))
}
