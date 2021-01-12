package cc.cryptopunks.crypton.dependency

import cc.cryptopunks.crypton.Dependency
import cc.cryptopunks.crypton.create.depKey
import kotlinx.coroutines.CoroutineScope

inline fun <reified T> CoroutineScope.get(key: Dependency.Key<T> = depKey()): T? =
    coroutineContext[key]?.run { get() }
