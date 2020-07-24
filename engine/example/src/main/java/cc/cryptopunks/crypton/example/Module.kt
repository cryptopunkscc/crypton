package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Context
import cc.cryptopunks.crypton.HandlerRegistry
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

internal data class AppModule(
    override val entityRepo: Entity.Repo = EntityRepo(),
    override val handlers: HandlerRegistry = getAllHandlers()
) : AppScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    override val nestedScopes: Map<String, NestedScope> =
        (0..10).map { "$it" }.associateWith {
            NestedModule(this, it)
        }

    override val log = javaClass.simpleName.typedLog()

    override suspend fun resolve(context: Context): Pair<Scope, Any> =
        nestedScopes.getValue(context.id) to context.any
}

private data class NestedModule(
    val appScope: AppScope,
    override val id: String
) : NestedScope,
    AppScope by appScope {

    override suspend fun resolve(context: Context): Pair<Scope, Any> = when (id) {
        context.id -> this to context.any
        else -> appScope resolve context
    }
}
