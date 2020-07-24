package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.ConnectableBinding
import cc.cryptopunks.crypton.ConnectorOutput
import cc.cryptopunks.crypton.actor
import cc.cryptopunks.crypton.connectable
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.createHandlers
import cc.cryptopunks.crypton.handler.handleConferenceInvitations
import cc.cryptopunks.crypton.handler.handleFlushMessageQueue
import cc.cryptopunks.crypton.handler.handleJoin
import cc.cryptopunks.crypton.handler.handlePresenceChanged
import cc.cryptopunks.crypton.handler.handleSaveMessages
import cc.cryptopunks.crypton.handler.handleUpdateChatNotification
import cc.cryptopunks.crypton.selector.accountAuthenticatedFlow
import cc.cryptopunks.crypton.selector.flushMessageQueueFlow
import cc.cryptopunks.crypton.selector.joinConferencesFlow
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import cc.cryptopunks.crypton.selector.saveMessagesFlow
import cc.cryptopunks.crypton.selector.updateChatNotificationFlow
import handleAccountAuthenticated
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal fun SessionScope.startSessionService(out: ConnectorOutput = {}) {
    log.d("Invoke session services for $address")
    ConnectableBinding() + connectable() + actor { (_, _, out) ->
        launch { accountActionsFlow().collect(out) }
    }
}

val sessionHandlers = createHandlers {
    +handleSaveMessages()
    +handlePresenceChanged()
    +handleFlushMessageQueue()
    +handleConferenceInvitations()
    +handleUpdateChatNotification()
    +handleAccountAuthenticated()
    +handleJoin()
}

internal fun SessionScope.accountActionsFlow() = flowOf(
    saveMessagesFlow(),
    presenceChangedFlow(),
    flushMessageQueueFlow(),
    conferenceInvitationsFlow(),
    updateChatNotificationFlow(),
    accountAuthenticatedFlow(),
    joinConferencesFlow()
).flattenMerge().flowOn(Dispatchers.IO).onStart {
    log.d("start accountActionsFlow")
}.onCompletion {
    log.d("stop accountActionsFlow")
}.onEach {
    log.d("each accountActionsFlow $it")
}
