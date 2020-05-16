package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.mock.MockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlin.random.Random

class MessageNetMock(
    private val state: MockState
) : Message.Net {

    override suspend fun sendMessage(address: Address, message: String) {
        state {
            val id = String(Random.nextBytes(16))
            messageEvents.send(
                Message.Net.Event.Sent(
                    Message(
                        id = id,
                        stanzaId = id,
                        chatAddress = address,
                        from = state.defaults.resource,
                        to = Resource(address, "mock"),
                        status = Message.Status.Read,
                        notifiedAt = System.currentTimeMillis(),
                        readAt = 0,
                        timestamp = System.currentTimeMillis(),
                        text = message
                    )
                )
            )
        }
    }

    override fun readArchived(
        query: Message.Net.ReadArchived.Query
    ): Flow<List<Message>> = state.defaults.messages.asFlow()

    override fun messageEvents(): Flow<Message.Net.Event> = state.messageEvents.consumeAsFlow()

}
