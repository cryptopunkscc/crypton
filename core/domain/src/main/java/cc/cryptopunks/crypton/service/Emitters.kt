package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.selector.flushMessageQueueFlow
import cc.cryptopunks.crypton.selector.handlePresenceFlow
import cc.cryptopunks.crypton.selector.hasAccountsFlow
import cc.cryptopunks.crypton.selector.joinConferencesFlow
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import cc.cryptopunks.crypton.selector.saveMessagesFlow
import cc.cryptopunks.crypton.selector.sessionActionsFlow
import cc.cryptopunks.crypton.selector.startSessionServicesFlow
import cc.cryptopunks.crypton.selector.syncConferencesFlow
import cc.cryptopunks.crypton.selector.toggleIndicatorFlow
import cc.cryptopunks.crypton.selector.updateChatNotificationFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart


internal fun RootScope.appActionsFlow() = flowOf(
    toggleIndicatorFlow(),
    sessionActionsFlow(),
    startSessionServicesFlow()
).flattenMerge().onEach {
    log.d("App action flow $it")
}

internal fun SessionScope.accountActionsFlow() = flowOf(
    saveMessagesFlow(),
    handlePresenceFlow(),
    flushMessageQueueFlow(),
    conferenceInvitationsFlow(),
    updateChatNotificationFlow(),
    syncConferencesFlow(),
    joinConferencesFlow()
).flattenMerge().flowOn(Dispatchers.IO).onStart {
    log.d("start accountActionsFlow")
}.onCompletion {
    log.d("stop accountActionsFlow")
}
