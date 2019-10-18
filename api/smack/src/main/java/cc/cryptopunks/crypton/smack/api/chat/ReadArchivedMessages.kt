package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.util.ext.hasOmemoExtension
import cc.cryptopunks.crypton.smack.util.ext.replaceBody
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jivesoftware.smackx.forward.packet.Forwarded
import org.jivesoftware.smackx.mam.MamManager
import org.jivesoftware.smackx.omemo.OmemoManager
import java.util.*

internal class ReadArchivedMessages(
    private val mamManager: MamManager,
    private val omemoManager: OmemoManager
) : Message.Api.ReadArchived {

    override fun invoke(
        query: Message.Api.ReadArchived.Query
    ): Flow<List<Message>> = query
        .asMamQueryArgs()
        .let(mamManager::queryArchive)
        .flowMessages()


    private fun Message.Api.ReadArchived.Query.asMamQueryArgs() = MamManager.MamQueryArgs
        .builder()
        .run {
            since?.let { limitResultsSince(Date(it)) }
            limitResultsBefore(Date(until))
            setResultPageSizeTo(PAGE_SIZE)
        }
        .build()

    private fun MamManager.MamQuery.flowMessages() = flow {
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
                .let { emit(it) }
            pageNext(PAGE_SIZE)
        }
    }

    private companion object {
        const val PAGE_SIZE = 100
    }
}