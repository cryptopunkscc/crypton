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
import org.jivesoftware.smackx.muc.packet.MUCUser
import org.jxmpp.jid.EntityJid
import org.jxmpp.jid.parts.Resourcepart
import java.util.Date

internal class ChatNet(
    private val smackCore: SmackCore,
    private val account: Address
) :
    SmackCore by smackCore,
    Chat.Net {

    override fun supportEncryption(address: Address): Boolean =
        omemoManager.multiUserChatSupportsOmemo(mucManager.getMultiUserChat(address.entityBareJid()))


    override fun createOrJoinConference(chat: Chat): Chat = smackCore.createOrJoinConference(chat)

    override fun configureConference(chat: Address) = smackCore.configureConference(chat)

    override fun inviteToConference(chat: Address, users: Set<Address>) =
        smackCore.inviteToConference(chat, users)

    override fun conferenceInvitationsFlow(): Flow<Chat.Invitation> =
        smackCore.invitationsFlow()

    override fun joinConference(
        address: Address,
        nickname: String,
        historySince: Long
    ): Unit = mucManager.run {
        getMultiUserChat(address.entityBareJid()).run {
            join(
                getEnterConfigurationBuilder(Resourcepart.from(nickname))
                    .requestHistorySince(Date(historySince) )
                    .build()
            )
        }
    }

    override fun listJoinedRooms(): Set<Address> =
        mucManager.joinedRooms.map { it.address() }.toSet()

    override fun listHostedRooms(): Set<Address> =
        mucManager.mucServiceDomains.map(mucManager::getHostedRooms).flatten()
            .map { it.jid.address() }.toSet()

    override fun getChatInfo(address: Address): Chat.Info {
        val conference = mucManager.getMultiUserChat(address.entityBareJid())
        val info = mucManager.getRoomInfo(address.entityBareJid())
        return Chat.Info(
            account = account,
            address = address,
            name = info.name,
            members = conference.run { owners + admins + members }.map {
                Chat.Member(
                    nick = it.nick?.run { toString() },
                    address = it.jid.address(),
                    role = it.role?.run { Chat.Role.valueOf(name) } ?: Chat.Role.unknown,
                    affiliation = it.affiliation?.run { Chat.Affiliation.valueOf(name) }
                        ?: Chat.Affiliation.unknown
                )
            }.toSet()
        )
    }
}

internal fun SmackCore.inviteToConference(
    chat: Address,
    users: Set<Address>
) {
    log.d { "Muc inviting $users" }
    mucManager.getMultiUserChat(chat.entityBareJid()).apply {
        users.forEach { user ->
            invite(user.entityBareJid(), "no reason")
        }
    }
}

internal fun SmackCore.createOrJoinConference(
    chat: Chat
) = chat.also {
    log.d { "Creating conference $chat" }
    mucManager.getMultiUserChat(chat.address.entityBareJid())
        .createOrJoin(Resourcepart.from(chat.account.local))
}

internal fun SmackCore.configureConference(
    chat: Address
) {
    log.d { "Configuring conference $chat" }
    mucManager.getMultiUserChat(chat.entityBareJid()).apply {
        sendConfigurationForm(
            configurationForm.createAnswerForm().apply {
                setAnswer(Muc.RoomConfig.MEMBERS_ONLY, true)
                setAnswer(Muc.RoomConfig.PUBLIC_ROOM, false)
                setAnswer(Muc.RoomConfig.ALLOW_INVITES, true)
                setAnswer(Muc.RoomConfig.PERSISTENT_ROOM, true)
            }
        )
    }
}

internal fun SmackCore.invitationsFlow() =
    callbackFlow<Chat.Invitation> {
        InvitationListener { conn: XMPPConnection,
                             room: MultiUserChat,
                             inviter: EntityJid,
                             reason: String?,
                             password: String?,
                             message: Message,
                             invitation: MUCUser.Invite ->
            offer(
                Chat.Invitation(
                    address = room.room.address(),
                    inviter = inviter.resource(),
                    password = password,
                    reason = reason
                ).also {
                    log.d { "new invitation $it" }
                }
            )
        }.let { listener ->
            mucManager.addInvitationListener(listener)
            awaitClose {
                mucManager.removeInvitationListener(listener)
            }
        }
    }
