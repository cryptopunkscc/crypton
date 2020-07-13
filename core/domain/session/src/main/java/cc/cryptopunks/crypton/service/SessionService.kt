package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.handler.handleApiEvent
import cc.cryptopunks.crypton.handler.handleFlushQueuedMessages
import cc.cryptopunks.crypton.handler.handleConferenceInvitations
import cc.cryptopunks.crypton.handler.handlePresenceChanged
import cc.cryptopunks.crypton.handler.handleSaveMessages
import cc.cryptopunks.crypton.handler.handleUpdateChatNotification
import cc.cryptopunks.crypton.selector.flushQueueMessagesFlow
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import cc.cryptopunks.crypton.selector.saveMessagesFlow
import cc.cryptopunks.crypton.selector.updateChatNotificationFlow
import kotlinx.coroutines.launch

fun SessionScope.startSessionService() = launch {
    log.d("Invoke session services for $address")
    launch { netEvents().collect(handleApiEvent()) }
    launch { updateChatNotificationFlow().collect(handleUpdateChatNotification() ) }
    launch { saveMessagesFlow().collect(handleSaveMessages(), join = true) }
    launch { presenceChangedFlow().collect(handlePresenceChanged(), join = true) }
    launch { flushQueueMessagesFlow().collect(handleFlushQueuedMessages(), join = true) }
    launch { conferenceInvitationsFlow().collect(handleConferenceInvitations()) }
}
