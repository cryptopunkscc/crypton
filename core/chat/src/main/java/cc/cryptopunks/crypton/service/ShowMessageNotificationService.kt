package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.api.Api
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.entity.Message
import cc.cryptopunks.crypton.presenter.ChatPresenter
import cc.cryptopunks.crypton.presenter.RosterPresenter
import cc.cryptopunks.crypton.presentation.Presentation
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.util.ext.any
import cc.cryptopunks.crypton.util.log
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class ShowMessageNotificationService @Inject constructor(
    private val scope: Api.Scope,
    private val address: Address,
    private val presentationManager: PresentationManager,
    private val showNotification: Message.Sys.ShowNotification,
    private val messageBroadcast: Message.Api.Broadcast
) : () -> Job {

    override fun invoke() = scope.launch {
        log<ShowMessageNotificationService>("start")
        messageBroadcast
            .filterNot { it.from.address == address }
            .filterNot { canConsume(it) }
            .collect { showNotification(it) }
        log<ShowMessageNotificationService>("stop")
    }

    private fun canConsume(
        message: Message
    ): Boolean = presentationManager
        .stack()
        .any { presenter -> presenter.canConsume(message) }

    private fun Presentation.Snapshot.canConsume(
        message: Message
    ) = isVisible && presenter.run {
        listOf(
            this is RosterPresenter,
            this is ChatPresenter && message.chatAddress == chat.address
        ) any true
    }
}