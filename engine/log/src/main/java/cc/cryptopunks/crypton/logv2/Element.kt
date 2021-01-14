package cc.cryptopunks.crypton.logv2

import kotlin.coroutines.CoroutineContext

class LogElement<S, D>(
    log: Log<S, D>,
) : Log<S, D> by log,
    CoroutineContext.Element {
    data class Key<S, D>(val type: Class<LogElement<S, D>>) : CoroutineContext.Key<LogElement<S, D>>

    override val key: Key<S, D> = Key(javaClass)
}
