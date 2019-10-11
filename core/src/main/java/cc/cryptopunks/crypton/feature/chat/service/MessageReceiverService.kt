package cc.cryptopunks.crypton.feature.chat.service

import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.util.Scope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class MessageReceiverService @Inject constructor(
    scope: Scope.Service,
    messageBroadcast: Message.Api.Broadcast,
    messageRepo: Message.Repo,
    chatRepo: Chat.Repo
) : () -> Job by {
    scope.launch {
        messageBroadcast.collect { message ->
            chatRepo.get(message.to.address)?.let { chat ->
                messageRepo.insertOrUpdate(chat.id, message)
            }
        }
    }
}