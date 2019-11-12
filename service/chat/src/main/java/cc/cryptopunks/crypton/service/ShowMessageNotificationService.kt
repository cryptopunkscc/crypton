package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.entity.Session
import cc.cryptopunks.crypton.entity.canConsume
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class ShowMessageNotificationService @Inject constructor(
    private val scope: Session.Scope,
    private val address: Address,
    private val presentationManager: PresentationManager,
    private val showNotification: Message.Sys.ShowNotification,
    private val messageBroadcast: Message.Net.Broadcast
) : () -> Job {

    override fun invoke() = scope.launch {
        log<ShowMessageNotificationService>("start")
        messageBroadcast
            .filterNot { it.from.address == address }
            .filterNot { canConsumeMessage(it) }
            .collect { showNotification(it) }
        log<ShowMessageNotificationService>("stop")
    }

    private fun canConsumeMessage(
        message: Message
    ): Boolean = presentationManager
        .stack()
        .filter { it.isVisible }
        .any { it.presenter.canConsume(message) }
}