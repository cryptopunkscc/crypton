package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.TypedLog
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private class FlushQueueMessages

fun Session.createFlushQueuedMessages(
    log: TypedLog = FlushQueueMessages().typedLog()
): () -> Job = {
    scope.launch {
        log.d("Start")
        messageRepo.queuedListFlow().collect { list ->
            log.d("Flush queue $list")
            list.forEach { message ->
                sendMessage(message)
            }
        }
    }
}
