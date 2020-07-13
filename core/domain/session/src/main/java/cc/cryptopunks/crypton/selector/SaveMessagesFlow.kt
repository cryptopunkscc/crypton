package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Net
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun SessionScope.saveMessagesFlow() = flowOf(
    netEvents().filterIsInstance<Net.OmemoInitialized>().flatMapConcat {
        fetchArchivedMessages().onEach { log.d("Archived messages $it") }
    },
    incomingMessages().onEach { log.d("Incoming message $it ") }.map {
        listOf(it.message)
    }
).flattenMerge().map {
    Message.Service.Save(it)
}
