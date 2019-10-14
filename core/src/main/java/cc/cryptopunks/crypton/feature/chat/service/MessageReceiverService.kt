package cc.cryptopunks.crypton.feature.chat.service

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Chat
import cc.cryptopunks.crypton.entity.Message
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

class MessageReceiverService @Inject constructor(
    scope: Api.Scope,
    messageBroadcast: Message.Api.Broadcast,
    messageRepo: Message.Repo,
    chatRepo: Chat.Repo
) : () -> Job by {
    scope.plus(SupervisorJob()).launch {
        messageBroadcast.collect { message ->
            scope.launch {
                chatRepo.get(message.chatAddress).let {
                    messageRepo.insertOrUpdate(message)
                }
            }
        }
    }
}