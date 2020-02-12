package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.interactor.SaveMessagesInteractor
import cc.cryptopunks.crypton.util.ext.invokeOnClose
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@SessionScope
class MessageReceiverService @Inject constructor(
    private val scope: Session.Scope,
    private val messageEvents: Message.Net.Events,
    private val saveMessages: SaveMessagesInteractor
) : () -> Job {

    private val log = typedLog()

    override fun invoke(): Job = scope.launch {
        log.d("start")
        invokeOnClose { log.d("stop") }
        messageEvents.collect { event ->
            log.d("message event received: $event")
            saveMessages(event)
        }
    }
}