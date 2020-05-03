package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.mock.MockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow

class MessageNetMock(
    private val state: MockState
) : Message.Net {

    override val sendMessage = object :
        Message.Net.Send {
        override suspend fun invoke(address: Address, messageText: String) {
            state {
                val id = System.nanoTime().toString()
                messageEvents.send(Message.Net.Event.Sent(
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
                        text = messageText
                    )
                ))
            }
        }
    }

    override val messageEvents: Message.Net.Events = object :
        Message.Net.Events,
        Flow<Message.Net.Event> by state.messageEvents.consumeAsFlow() {}

    override val readArchived: Message.Net.ReadArchived = object :
        Message.Net.ReadArchived {
        override fun invoke(query: Message.Net.ReadArchived.Query): Flow<List<Message>> =
            state.defaults.messages.asFlow()
    }
}
