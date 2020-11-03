package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.emitter.flushMessageQueueFlow
import cc.cryptopunks.crypton.emitter.handlePresenceFlow
import cc.cryptopunks.crypton.emitter.insertInvitationFlow
import cc.cryptopunks.crypton.emitter.joinConferencesFlow
import cc.cryptopunks.crypton.emitter.reconnectSessionFlow
import cc.cryptopunks.crypton.emitter.saveMessagesFlow
import cc.cryptopunks.crypton.emitter.startSessionServicesFlow
import cc.cryptopunks.crypton.emitter.syncConferencesFlow
import cc.cryptopunks.crypton.emitter.toggleIndicatorFlow
import cc.cryptopunks.crypton.emitter.updateChatNotificationFlow
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


internal fun RootScope.appActionsFlow() = flowOf(
    toggleIndicatorFlow(),
//    sessionActionsFlow(),
    startSessionServicesFlow()
).flattenMerge().onStart {
    log.builder.d { status = Log.Event.Status.Start.name }
}.onCompletion {
    log.builder.d { status = Log.Event.Status.Finished.name }
}

internal fun SessionScope.accountActionsFlow() = flowOf(
    reconnectSessionFlow(),
    saveMessagesFlow(),
    handlePresenceFlow(),
    flushMessageQueueFlow(),
    insertInvitationFlow(),
    updateChatNotificationFlow(),
    syncConferencesFlow(),
    joinConferencesFlow()
).flattenMerge().onStart {
    log.builder.d { status = Log.Event.Status.Start.name }
}.onCompletion {
    log.builder.d { status = Log.Event.Status.Finished.name }
}.flowOn(Dispatchers.IO)
