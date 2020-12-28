package cc.cryptopunks.crypton

import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull


typealias Resolve = suspend Scope.(Any) -> Any?

val Scope.resolvers: Resolvers by dep()

class Resolvers(list: List<Resolve> = emptyList()): List<Resolve> by list

fun resolvers(
    vararg args: Resolve
) = Resolvers(args.toList())

operator fun Resolvers.plus(other: List<Resolve>) = Resolvers(toList() + other)

internal suspend fun Resolvers.resolve(
    scope: Scope,
    any: Any
): Scoped.Resolved =
    resolveRecursive(scope, any)
        ?: Scoped.Resolved(scope, CannotResolve(any))

private tailrec suspend fun Resolvers.resolveRecursive(
    scope: Scope,
    any: Any?
): Scoped.Resolved? {
    println("resolving: $any, job: ${scope.coroutineContext[Job]}, tag: ${scope.coroutineContext[ScopeTag]}")
    return when (any) {
        null -> null
        is Scoped.Resolved -> any
        else -> resolveRecursive(scope, first(scope, any))
    }
}

private suspend fun Resolvers.first(scope: Scope, any: Any): Any? =
    asFlow().mapNotNull { it(scope, any) }.firstOrNull()
