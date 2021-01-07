package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped
import cc.cryptopunks.crypton.create.cryptonContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal data class RootModule(
    override val entityRepo: Entity.Repo = EntityRepo(),
) : RootScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO +
        cryptonContext(
            getAllHandlers()
        )

    override val nestedScopes: Map<String, NestedScope> =
        (0..10).map { "$it" }.associateWith {
            NestedModule(this, it)
        }

    override suspend fun resolve(context: Scoped): Pair<Scope, Any> =
        nestedScopes.getValue(context.id) to context.next
}

private data class NestedModule(
    val rootScope: RootScope,
    override val id: String,
) : NestedScope,
    RootScope by rootScope {

    override suspend fun resolve(context: Scoped): Pair<Scope, Any> = when (id) {
        context.id -> this to context.next
        else -> rootScope resolve context
    }
}
