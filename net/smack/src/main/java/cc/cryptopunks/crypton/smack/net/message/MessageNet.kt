package cc.cryptopunks.crypton.smack.net.message

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.smack.SmackCore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map

internal class MessageNet(
    private val smack: SmackCore
) : SmackCore by smack,
    Message.Net {

    private val messageBroadcast = smack.createMessageEventBroadcast()

    override suspend fun sendMessage(message: Message, encrypt: Boolean): Job =
        smack.sendMessage(message, encrypt)

    override fun incomingMessages(): Flow<Message.Net.Incoming> =
        messageBroadcast.asFlow().map { Message.Net.Incoming(it) }

    override fun readArchived(query: Message.Net.ReadArchived.Query): Flow<List<Message>> =
        smack.readArchivedMessages(query)


}
