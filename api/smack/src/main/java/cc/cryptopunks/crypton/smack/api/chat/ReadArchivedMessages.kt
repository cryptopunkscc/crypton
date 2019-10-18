package cc.cryptopunks.crypton.smack.api.chat

import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.smack.util.toCryptonMessage
import org.jivesoftware.smack.packet.Message as ApiMessage
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
    ): Flow<List<Message>> = MamManager.MamQueryArgs.builder().run {
        query.since?.let { limitResultsSince(Date(it)) }
        limitResultsBefore(Date(query.until))
        setResultPageSizeTo(PAGE_SIZE)
        build()
    }.let {
        mamManager.queryArchive(it).flowMessages()
    }

    private fun MamManager.MamQuery.flowMessages() = flow {
        while (messageCount > 0) {
            val decryptedQueryResult = omemoManager.decryptMamQueryResult(this@flowMessages)
            page.forwarded
                .mapIndexed { index: Int, forwarded: Forwarded ->
                    val decrypted = decryptedQueryResult[index]
                    forwarded.apply {
                        if (decrypted.isOmemoMessage)
                            (forwardedStanza as ApiMessage).body = decrypted.omemoMessage.body
                    }
                }
                .map { it.toCryptonMessage() }
                .let { emit(it) }

            pageNext(PAGE_SIZE)
        }
    }

    private companion object {
        const val PAGE_SIZE = 100
    }
}