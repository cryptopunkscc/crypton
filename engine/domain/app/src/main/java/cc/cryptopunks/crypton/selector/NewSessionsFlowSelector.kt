package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.onEach

class NewSessionsFlowSelector(
    private val store: SessionScope.Store
) : () -> Flow<SessionScope> {
    private val log = typedLog()
    override fun invoke(): Flow<SessionScope> {
        var sessions = emptyMap<Address, SessionScope>()
        return store.changesFlow().flatMapConcat { current ->
            val new = (current - sessions.keys)
            sessions = current
            new.map { it.value }.asFlow()
        }.onEach {
            log.d("New session ${it.address}")
        }
    }
}
