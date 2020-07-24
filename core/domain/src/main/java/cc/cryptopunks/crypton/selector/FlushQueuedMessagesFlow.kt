package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart


internal fun SessionScope.flushMessageQueueFlow(): Flow<Exec.FlushQueuedMessages> =
    flowOf(
        netEvents().filterIsInstance<Net.OmemoInitialized>().map {
            log.d("flush by Net.OmemoInitialized")
            messageRepo.queuedList().map { it.chat }
        }.bufferedThrottle(3000).map { it.last() },
        presenceChangedFlow().filter {
            it.presence.status == Presence.Status.Available
        }.map {
            log.d("flush by Presence.Status.Subscribed")
            listOf(it.presence.resource.address)
        },
        messageRepo.queuedListFlow().map { list ->
            log.d("flush by queuedListFlow $address $list")
            list.map { it.chat }
        }
    ).flattenMerge().filter {
        it.isNotEmpty() && isOmemoInitialized().also {
            log.d("flush omemo initialized: $it")
        }
    }.onStart {
        log.d("start flushMessageQueueFlow")
    }.onCompletion {
        log.d("complete flushMessageQueueFlow")
    }.map {
        Exec.FlushQueuedMessages(it.toSet())
    }
