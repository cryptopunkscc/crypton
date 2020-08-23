package cc.cryptopunks.crypton.emitter

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.selector.archivedMessagesFlow
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun SessionScope.saveMessagesFlow() = flowOf(
    netEvents().filterIsInstance<Net.OmemoInitialized>().flatMapConcat {
        archivedMessagesFlow().onEach {
            log.d { "Archived messages" }
        }
    },
    incomingMessages().onEach {
        log.d { "Incoming message" }
    }.map {
        listOf(it.message)
    }
).flattenMerge().map {
    Exec.SaveMessages(it)
}
