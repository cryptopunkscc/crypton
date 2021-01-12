package cc.cryptopunks.crypton.execute

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Execute
import cc.cryptopunks.crypton.Resolver
import kotlinx.coroutines.CoroutineScope

internal val resolveAction: Execute = {
    copy(
        action = try {
            resolve(root, arg)
        } catch (e: Throwable) {
            throw IllegalArgumentException(arg.toString(), e)
        }
            ?: throw IllegalArgumentException("Cannot find resolver for $arg")
    )
}

private tailrec suspend fun resolve(scope: CoroutineScope, arg: Any?): Action? = when (arg) {
    null -> null
    is Action -> arg
    else -> resolve(
        scope = scope,
        arg = scope.resolveFor(arg)?.run { scope.resolve(arg) }
    )
}

private fun CoroutineScope.resolveFor(arg: Any) = coroutineContext[Resolver.Key(arg.javaClass)]
