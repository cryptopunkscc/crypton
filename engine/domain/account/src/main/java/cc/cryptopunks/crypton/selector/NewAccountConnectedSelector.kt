package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.*

class NewAccountConnectedSelector internal constructor(
    private val sessionsStore: SessionScope.Store
) {

    operator fun invoke(): Flow<Address> = mutableSetOf<SessionScope>().let { cache ->
        sessionsStore.changesFlow()
            .flatMapConcat { map -> map.values.asFlow() }
            .filterNot { session -> session in cache }
            .flatMapMerge { session ->
                session.netEvents()
                    .produceIn(session.scope)
                    .consumeAsFlow()
                    .onCompletion { cache.remove(session) }
                    .filterIsInstance<Account.Authenticated>()
                    .mapNotNull {
                        if (session in cache) null
                        else session.also { cache.add(it) }.address
                    }
            }
    }
}
