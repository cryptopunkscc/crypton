package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.handler.handleApiEvent
import cc.cryptopunks.crypton.handler.handleOmemoInitialized
import cc.cryptopunks.crypton.handler.handlePresenceChanged
import cc.cryptopunks.crypton.interactor.saveMessage
import cc.cryptopunks.crypton.interactor.updateChatNotification
import cc.cryptopunks.crypton.selector.omemoInitializations
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.launch

fun SessionScope.startSessionService() = launch {
    log.d("Invoke session services for $address")
    launch { netEvents().collect(handleApiEvent()) }
    launch { omemoInitializations().collect(handleOmemoInitialized()) }
    launch {
        messageRepo.unreadListFlow().fold(updateChatNotification()) { update, messages ->
            update.also { it(messages) }
        }
    }
    launch { incomingMessages().collect { event -> saveMessage(event.message) } }
    launch { presenceChangedFlow().collect(handlePresenceChanged()) }
}
