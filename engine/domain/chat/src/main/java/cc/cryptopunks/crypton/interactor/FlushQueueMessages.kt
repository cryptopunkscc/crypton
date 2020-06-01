package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun Session.createFlushQueuedMessages(): () -> Job = {
    scope.launch {
        messageRepo.queuedListFlow().collect { list ->
            list.forEach { message ->
                sendMessage(message)
            }
        }
    }
}
