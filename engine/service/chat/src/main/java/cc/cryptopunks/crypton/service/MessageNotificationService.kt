package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@SessionScope
class MessageNotificationService @Inject constructor(
    private val scope: Session.Scope,
    private val serviceManager: ServiceManager,
    private val receivedMessages: Message.Notify.Received,
    private val repo: Message.Repo
) : () -> Job {

    private val log = typedLog()

    private var current = emptyList<Message>()

    override fun invoke() = scope.launch {
        launch {
            log.d("start")
            repo.unreadListFlow().collect { messages ->
                receivedMessages - (current - messages)
                current = messages.consume()
                receivedMessages(current)
            }
            log.d("stop")
        }
    }

    private fun List<Message>.consume() = serviceManager.top()
        ?.services
        ?.filterIsInstance<Message.Consumer>()
        ?.let { consumers ->
            filterNot { message ->
                consumers.any { consumer ->
                    consumer.canConsume(message)
                }
            }
        }
        ?: this
}