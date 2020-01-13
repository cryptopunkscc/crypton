package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.annotation.SessionScope
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.canConsume
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@SessionScope
class MessageNotificationService @Inject constructor(
    private val scope: Session.Scope,
    private val address: Address,
    private val serviceManager: ServiceBindingManager,
    private val showNotification: Message.Sys.ShowNotification,
    private val messageBroadcast: Message.Net.Broadcast
) : () -> Job {

    private val log = typedLog()

    override fun invoke() = scope.launch {
        log.d("start")
        messageBroadcast
            .map { it.message }
            .filterNot { it.from.address == address }
            .filterNot { canConsumeMessage(it) }
            .collect { showNotification(it) }
        log.d("stop")
    }

    private fun canConsumeMessage(
        message: Message
    ): Boolean = serviceManager
        .stack()
        .filter { it.isVisible }
        .any { it.right.canConsume(message) }
}