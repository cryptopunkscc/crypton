package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScopeTag
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.context.net
import cc.cryptopunks.crypton.factory.emitter
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.interactor.flushQueuedMessages
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.logv2.v
import cc.cryptopunks.crypton.selector.presenceChangedFlow
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

internal fun flushMessageQueue() = feature(

    emitter = emitter(SessionScopeTag) {
        val net = net
        val messageRepo = messageRepo
        flowOf(
            net.netEvents().filterIsInstance<Net.OmemoInitialized>().map {
                Net.OmemoInitialized to messageRepo.listQueued().map { it.chat }
            }.bufferedThrottle(3000).map { it.last() },
            presenceChangedFlow().filter {
                it.presence.status == Presence.Status.Available
            }.map {
                it to listOf(it.presence.resource.address)
            },
            messageRepo.flowListQueued().map { list ->
                "flowListQueued" to list.map { it.chat }
            }
        ).flattenMerge().filter { (_, addresses) ->
            addresses.isNotEmpty() && net.isOmemoInitialized()
        }.onEach {
            log.v { "flush omemo initialized: $it" }
        }.onStart {
            log.v { "start flushMessageQueueFlow" }
        }.onCompletion {
            log.v { "complete flushMessageQueueFlow" }
        }.map {
            Exec.FlushQueuedMessages(it.second.toSet())
        }
    },

    handler = handler { _, (addresses): Exec.FlushQueuedMessages ->
        if (addresses.isEmpty())
            flushQueuedMessages { true } else
            flushQueuedMessages { message: Message -> addresses.contains(message.chat) }
    }
)
