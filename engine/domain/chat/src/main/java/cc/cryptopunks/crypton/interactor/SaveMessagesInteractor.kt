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
            .map { it.get() ?: it.create() }
            .let { messageRepo.insertOrUpdate(it) }
    }

    suspend operator fun invoke(event: Message.Net.Event) {
        val message = event.message.run {
            when(event) {
                is Message.Net.Event.Sending -> create()
                else -> get() ?: create()
            }
        }.copy(
            status = when (event) {
                is Message.Net.Event.Queued -> Message.Status.Queued
                is Message.Net.Event.Sending -> Message.Status.Sending
                is Message.Net.Event.Sent -> Message.Status.Sent
                is Message.Net.Event.Received -> Message.Status.Received
            }
        )

        log.d("inserting message $message")
        messageRepo.run {
            insertOrUpdate(message)
            if (message.status == Message.Status.Received)
                notifyUnread()
        }
    }

    private suspend fun Message.get() = messageRepo.run {
        get(stanzaId)?.also {
            delete(it)
        }
    }

    private suspend fun Message.create() = copy(
        chatAddress = createChat(
            CreateChatInteractor.Data(
                title = chatAddress.id,
                users = listOf(
                    User(
                        getParty(address).address
                    )
                )
            )
        ).await().address
    )
}