package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.RequestLog
import cc.cryptopunks.crypton.Resolved
import cc.cryptopunks.crypton.Resolver
import cc.cryptopunks.crypton.logv2.d
import cc.cryptopunks.crypton.util.contains
import kotlinx.coroutines.CoroutineScope

internal val resolveScope: Execute = {
    resolveRecursive(root, action).run {
        log.d { RequestLog.Event.Resolved }
        copy(
            action = action,
            scope = scope
        )
    }
}

private tailrec suspend fun resolveRecursive(
    scope: CoroutineScope,
    input: Any,
): Action.Resolved =
    when (input) {
        is Action.Resolved -> input
        else -> resolveRecursive(scope, resolve(scope, input))
    }

private suspend fun resolve(
    scope: CoroutineScope,
    input: Any,
): Any = scope.resolver(input)
    ?.resolve?.invoke(scope, input)
    ?: when {
        Resolver.ThrowOnUnknown in scope -> throw cannotFindResolver(input)
        input is Action -> Resolved(scope, input)
        else -> throw cannotFindResolver(input)
    }

private fun CoroutineScope.resolver(
    input: Any,
): Resolver? =
    coroutineContext[input.resolverKey()]

private fun Any.resolverKey() =
    Resolver.Key(javaClass)

private fun cannotFindResolver(any: Any) =
    IllegalArgumentException("Cannot find resolver for $any")

