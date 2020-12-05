package cc.cryptopunks.crypton

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull


typealias Resolve = suspend Scope.(Any) -> Any?
typealias Resolvers = List<Resolve>

internal suspend fun Resolvers.resolve(
    scope: Scope,
    any: Any
): Scoped.Resolved =
    resolveRecursive(scope, any)
        ?: Scoped.Resolved(scope, CannotResolve(any))

private tailrec suspend fun Resolvers.resolveRecursive(
    scope: Scope,
    any: Any?
): Scoped.Resolved? =
    when (any) {
        null -> null
        is Scoped.Resolved -> any
        else -> resolveRecursive(scope, first(scope, any))
    }

private suspend fun Resolvers.first(scope: Scope, any: Any): Any? =
    asFlow().mapNotNull { it(scope, any) }.firstOrNull()
