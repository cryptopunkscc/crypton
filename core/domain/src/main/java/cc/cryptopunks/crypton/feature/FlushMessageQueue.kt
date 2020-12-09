package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.emitter
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.flushQueuedMessages
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

internal fun flushMessageQueue() = feature(

    emitter = emitter<SessionScope> {
        flowOf(
            netEvents().filterIsInstance<Net.OmemoInitialized>().map {
                log.v { "flush by Net.OmemoInitialized" }
                messageRepo.listQueued().map { it.chat }
            }.bufferedThrottle(3000).map { it.last() },
            presenceChangedFlow().filter {
                it.presence.status == Presence.Status.Available
            }.map {
                log.v { "flush by Presence.Status.Subscribed" }
                listOf(it.presence.resource.address)
            },
            messageRepo.flowListQueued().map { list ->
                log.v { "flush by queuedListFlow $account $list" }
                list.map { it.chat }
            }
        ).flattenMerge().filter {
            it.isNotEmpty() && isOmemoInitialized().also {
                log.v { "flush omemo initialized: $it" }
            }
        }.onStart {
            log.v { "start flushMessageQueueFlow" }
        }.onCompletion {
            log.v { "complete flushMessageQueueFlow" }
        }.map {
            Exec.FlushQueuedMessages(it.toSet())
        }
    },

    handler = { _, (addresses): Exec.FlushQueuedMessages ->
        if (addresses.isEmpty())
            flushQueuedMessages { true } else
            flushQueuedMessages { message: Message -> addresses.contains(message.chat) }
    }
)
