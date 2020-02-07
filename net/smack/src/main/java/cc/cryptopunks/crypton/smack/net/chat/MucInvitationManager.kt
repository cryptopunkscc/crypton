package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.util.toChat
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.asFlow
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smackx.muc.InvitationListener
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.packet.MUCUser
import org.jxmpp.jid.EntityJid
import org.jxmpp.jid.parts.Resourcepart

internal class Join : Chat.Net.EventFlow {


    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Chat.Event>) {

    }
}


internal class MucInvitationManager(
    private val address: Address
) :
    InvitationListener,
    Chat.Net.EventFlow {

    private val channel = BroadcastChannel<Chat.Event>(Channel.BUFFERED)

    override fun invitationReceived(
        conn: XMPPConnection,
        room: MultiUserChat,
        inviter: EntityJid,
        reason: String?,
        password: String?,
        message: Message?,
        invitation: MUCUser.Invite
    ) {
        room.run {
            val nickname = conn.getNickname()
            val config = getEnterConfigurationBuilder(nickname)
                .withPassword(password)
                .build()
            join(config)
        }
        val event = Chat.Joined(room.toChat(address))
        channel.sendBlocking(event)
    }

    private fun XMPPConnection.getNickname() = user.localpart
        .asUnescapedString()
        .let(Resourcepart::from)

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Chat.Event>) {
        channel.asFlow().collect(collector)
    }
}