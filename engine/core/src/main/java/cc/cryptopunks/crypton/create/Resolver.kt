package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Resolver
import kotlinx.coroutines.CoroutineScope

inline fun <reified T> resolver(
    noinline resolve: suspend CoroutineScope.(T) -> Any
) = Resolver(
    key = Resolver.Key(T::class.java),
    resolve = resolve as suspend CoroutineScope.(Any) -> Any
)
