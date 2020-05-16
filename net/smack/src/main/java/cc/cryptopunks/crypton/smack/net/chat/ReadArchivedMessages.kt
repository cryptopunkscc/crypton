package cc.cryptopunks.crypton.smack.net.chat

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.CryptonMessage
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChat
import org.jivesoftware.smackx.muc.MultiUserChatManager
import org.jivesoftware.smackx.omemo.OmemoManager
import org.jxmpp.jid.impl.JidCreate
import java.util.*

internal class ReadArchivedMessages(
    private val connection: XMPPConnection,
    private val multiUserChatManager: MultiUserChatManager,
    private val omemoManager: OmemoManager
) {

    operator fun invoke(
        query: Message.Net.ReadArchived.Query
    ): Flow<List<Message>> = query.run {
        mamManager().queryArchive(mamQueryArgs()).flowMessages()
    }

    private fun Message.Net.ReadArchived.Query.mamManager() = chat
        ?.run { MamManager.getInstanceFor(getMuc()) }
        ?: MamManager.getInstanceFor(connection)

    private fun Chat.getMuc(): MultiUserChat = multiUserChatManager
        .getMultiUserChat(JidCreate.entityBareFrom(address.toString()))


    private fun Message.Net.ReadArchived.Query.mamQueryArgs() = MamManager.MamQueryArgs
        .builder()
        .run {
            since?.let { limitResultsSince(Date(it)) }
            afterUid?.let { afterUid(it) }
            limitResultsBefore(Date(until))
            setResultPageSizeTo(PAGE_SIZE)
        }
        .build()

    private fun MamManager.MamQuery.flowMessages(): Flow<List<CryptonMessage>> = flow {
        while (messageCount > 0) {
            val decryptedQueryResult = omemoManager.decryptMamQueryResult(this@flowMessages)
            page.forwarded
                .mapIndexedNotNull { index: Int, forwarded: Forwarded ->
                    if (!forwarded.forwardedStanza.hasOmemoExtension) forwarded
                    else decryptedQueryResult[index].takeIf { it.isOmemoMessage }?.run {
                        forwarded.takeIf { it.forwardedStanza.replaceBody(omemoMessage) != null }
                    }
                }
                .map(Forwarded::toCryptonMessage)
                .map { it.copy(readAt = System.currentTimeMillis()) }
                .filter { it.text.isNotBlank() }
                .let { emit(it) }
            pageNext(PAGE_SIZE)
        }
    }

    private companion object {
        const val PAGE_SIZE = 100
    }
}
