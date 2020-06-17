package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.mock.MockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow

class MessageNetMock(
    private val state: MockState
) : Message.Net {

    override suspend fun sendMessage(message: Message) {
        state { messageEvents.send(Message.Net.Event(message.copy(status = Message.Status.Sent))) }
    }

    override fun readArchived(
        query: Message.Net.ReadArchived.Query
    ): Flow<List<Message>> = state.defaults.messages.asFlow()

    override fun messageEvents(): Flow<Message.Net.Event> = state.messageEvents.consumeAsFlow()

}
