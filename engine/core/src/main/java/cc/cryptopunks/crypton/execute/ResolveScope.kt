package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Resolved
import cc.cryptopunks.crypton.Resolver
import cc.cryptopunks.crypton.util.contains
import kotlinx.coroutines.CoroutineScope

internal val resolveScope: Execute = {
    root.resolveRecursive(action).run {
        copy(
            action = action,
            scope = scope
        )
    }
}

internal tailrec fun CoroutineScope.resolveRecursive(
    input: Any,
): Action.Resolved =
    when (input) {
        is Action.Resolved -> input
        else -> resolveRecursive(resolve(input))
    }

private fun CoroutineScope.resolve(
    input: Any,
): Any = resolver(input)
    ?.resolve?.invoke(this, input)
    ?: when {
        contains(Resolver.ThrowOnUnknown) -> throw cannotFindResolver(input)
        input is Action -> Resolved(input, this)
        else -> throw cannotFindResolver(input)
    }

private fun CoroutineScope.resolver(
    input: Any,
): Resolver? =
    coroutineContext[input.resolverKey()]

private fun Any.resolverKey() =
    Resolver.Key(javaClass.name)

private fun cannotFindResolver(any: Any) =
    IllegalArgumentException("Cannot find resolver for $any")

