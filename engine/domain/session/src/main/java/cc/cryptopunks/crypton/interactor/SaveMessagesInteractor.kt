package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.User
import cc.cryptopunks.crypton.context.calculateId
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

internal class SaveMessagesInteractor(
    private val scope: Session.Scope,
    private val address: Address,
    private val messageRepo: Message.Repo,
    private val createChat: CreateChatInteractor
) : (List<Message>) -> Job {

    private val log = typedLog()

    override fun invoke(messages: List<Message>) = scope.launch {
        messages.forEach { invoke(it) }
    }

    suspend operator fun invoke(message: Message) {
        message.calculateId().run {
            get() ?: create()
        }.let { prepared ->
            messageRepo.run {
                log.d("inserting message $message")
                insertOrUpdate(prepared)
                if (prepared.status == Message.Status.Received)
                    notifyUnread()
            }
        }
    }

    private suspend fun Message.get() = messageRepo.run {
        get(id)?.also {
            delete(it)
        }
    }

    private suspend fun Message.create() = copy(
        chatAddress = createChat.invoke(
            CreateChatInteractor.Data(
                title = chatAddress.id,
                users = listOf(User(getParty(address).address))
            )
        ).address
    )
}
