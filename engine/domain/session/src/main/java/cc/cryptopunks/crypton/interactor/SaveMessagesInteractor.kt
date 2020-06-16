package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.context.createChat
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SaveMessagesInteractor(
    private val session: Session
) : (List<Message>) -> Job {

    private val log = typedLog()

    override fun invoke(messages: List<Message>) = session.scope.launch {
        messages.forEach { invoke(it) }
    }

    suspend operator fun invoke(message: Message) {
        message.run {
            get() ?: create()
        }.let { prepared ->
            session.messageRepo.run {
                log.d("inserting message $message")
                insertOrUpdate(prepared)
                if (prepared.status == Message.Status.Received)
                    notifyUnread()
            }
        }
    }

    private suspend fun Message.get() = session.messageRepo.run {
        get(id)?.also {
            delete(it)
        }?.copy(
            status = status
        )
    }

    private suspend fun Message.create() = copy(
        chatAddress = session.createChat(
            Chat.Service.CreateChatData(
                title = chatAddress.id,
                users = listOf(getParty(session.address).address)
            )
        ).address
    )
}
