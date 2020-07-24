package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.emitter.flushMessageQueueFlow
import cc.cryptopunks.crypton.emitter.handlePresenceFlow
import cc.cryptopunks.crypton.emitter.insertInvitationFlow
import cc.cryptopunks.crypton.emitter.joinConferencesFlow
import cc.cryptopunks.crypton.emitter.saveMessagesFlow
import cc.cryptopunks.crypton.emitter.sessionActionsFlow
import cc.cryptopunks.crypton.emitter.startSessionServicesFlow
import cc.cryptopunks.crypton.emitter.syncConferencesFlow
import cc.cryptopunks.crypton.emitter.toggleIndicatorFlow
import cc.cryptopunks.crypton.emitter.updateChatNotificationFlow
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
    insertInvitationFlow(),
    updateChatNotificationFlow(),
    syncConferencesFlow(),
    joinConferencesFlow()
).flattenMerge().flowOn(Dispatchers.IO).onStart {
    log.d("start accountActionsFlow")
}.onCompletion {
    log.d("stop accountActionsFlow")
}
