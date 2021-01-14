package cc.cryptopunks.crypton.delegate

import cc.cryptopunks.crypton.Dependency
import cc.cryptopunks.crypton.DynamicDependency
import cc.cryptopunks.crypton.create.depKey
import cc.cryptopunks.crypton.dependency.get
import kotlinx.coroutines.CoroutineScope

inline fun <reified T> dep(any: Any? = null) = DynamicDependency(depKey<T>(any ?: T::class.java))

inline fun <reified T> CoroutineScope.lazyDep(key: Dependency.Key<T> = depKey()) =
    lazy { get(key)!! }
