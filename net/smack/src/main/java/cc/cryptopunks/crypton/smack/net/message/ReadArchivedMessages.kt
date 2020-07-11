package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.CryptonMessage
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.SmackCore
import cc.cryptopunks.crypton.smack.util.entityBareJid
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.muc.MultiUserChat
import java.util.*

internal fun SmackCore.readArchivedMessages(
    query: Message.Net.ReadArchived.Query
) = flowMessages(mamManager.queryArchive(query.mamQueryArgs()))

private fun Message.Net.ReadArchived.Query.mamQueryArgs() = MamManager.MamQueryArgs
    .builder()
    .run {
        since?.let { limitResultsSince(Date(it)) }
        afterUid?.let { afterUid(it) }
        limitResultsBefore(Date(until))
        setResultPageSizeTo(PAGE_SIZE)
    }
    .build()

private fun SmackCore.flowMessages(query: MamManager.MamQuery): Flow<List<CryptonMessage>> = flow {
    while (query.messageCount > 0) {
        val decryptedQueryResult = omemoManager.decryptMamQueryResult(query)
        query.page.forwarded
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
        query.pageNext(PAGE_SIZE)
    }
}

const val PAGE_SIZE = 100
