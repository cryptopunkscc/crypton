package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import javax.inject.Inject

class SaveMessagesInteractor @Inject constructor(
    private val scope: Session.Scope,
    private val address: Address,
    private val messageRepo: Message.Repo,
    private val createChat: CreateChatInteractor
) : (List<Message>) -> Job {

    private val log = typedLog()

    override fun invoke(messages: List<Message>) = scope.launch {
        messages
            .map { Message.Event.Received(it) }
            .forEach { invoke(it) }
    }

    suspend operator fun invoke(event: Message.Event) = event.run {
        val message = when(event) {
            is Message.Event.Sending -> create()
            else -> get() ?: create()
        }.copy(
            status = when (event) {
                is Message.Event.Sending -> Message.Status.Sending
                is Message.Event.Sent -> Message.Status.Sent
                is Message.Event.Received -> Message.Status.Received
            }
        )

        log.d("inserting message $message")
        messageRepo.insertOrUpdate(message)
    }

    private suspend fun Message.Event.get() = messageRepo.run {
        get(message.stanzaId)?.also {
            delete(it)
        }
    }

    private suspend fun Message.Event.create() = message.copy(
        chatAddress = createChat(
            CreateChatInteractor.Data(
                title = message.chatAddress.id,
                users = listOf(
                    User(
                        message.getParty(address).address
                    )
                )
            )
        ).await().address
    )
}