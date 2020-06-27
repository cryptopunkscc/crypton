package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Message
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.consumeAsFlow

class MockMessageNet(
    private val state: MockState
) : Message.Net {

    override suspend fun sendMessage(message: Message): Job {
        delay(500)
        state { messageEvents.send(Message.Net.Event(message.copy(status = Message.Status.Sending))) }
        return Job().apply {
            delay(1000)
            state { messageEvents.send(Message.Net.Event(message.copy(status = Message.Status.Sent))) }
            complete()
        }
    }

    override suspend fun sendMucMessage(message: Message): Job {
        TODO("Not yet implemented")
    }

    override fun readArchived(
        query: Message.Net.ReadArchived.Query
    ): Flow<List<Message>> = state.defaults.messages.asFlow()

    override fun incomingMessages(): Flow<Message.Net.Event> = state.messageEvents.consumeAsFlow()

}
