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

    override suspend fun sendMessage(message: Message): Job =
        smack.sendMessage(message)

    override fun incomingMessages(): Flow<Message.Incoming> =
        messageBroadcast.asFlow().map { Message.Incoming(it) }

    override fun readArchived(query: Message.Net.ReadQuery): Flow<List<Message>> =
        smack.readArchivedMessages(query)


}
