package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


internal fun SessionScope.flushQueueMessagesFlow() = flowOf(
    omemoInitializations().map {
        messageRepo.unreadList().map { it.chatAddress }
    },
    presenceChangedFlow().filter {
        it.presence.status == Presence.Status.Subscribed
    }.map {
        listOf(it.presence.resource.address)
    },
    messageRepo.unreadListFlow().map { list ->
        list.map { it.chatAddress }
    }
).flattenMerge()
    .bufferedThrottle(200)
    .map { it.flatten() }
    .map {
        Chat.Service.FlushQueuedMessages(it.toSet())
    }
