package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.smack.util.toChat
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jxmpp.jid.parts.Resourcepart

internal class MultiUserChatProvider(
    private val account: Address,
    private val muc: MultiUserChatManager,
    private val invitationManager: MucInvitationManager,
    private val connection: XMPPConnection
) :
    Chat.Net.MultiUserChatFlow,
    Chat.Net.MultiUserChatList {

    override fun invoke(): List<Chat> = list().map {
        it.toChat(account)
    }

    @InternalCoroutinesApi
    override suspend fun collect(collector: FlowCollector<Chat>) {
        flow().map {
            it.toChat(account)
        }.collect(collector)
    }

    private fun flow(): Flow<MultiUserChat>  = emptyFlow()

//        flowOf<Chat>(
//        list().asFlow(),
//        invitationManager.filterIsInstance<Chat.Joined>().map { it.chat }
//    ).flattenConcat()

    private fun list(): List<MultiUserChat> = emptyList()

//        muc.run {
//        getMultiUserChat(
//            getHostedRooms(
//                mucServiceDomains.first()
//            ).first().jid
//        ).join(Resourcepart.from("yang"))
//        joinedRooms.map {
//            getMultiUserChat(it)
//        }
//    }
}