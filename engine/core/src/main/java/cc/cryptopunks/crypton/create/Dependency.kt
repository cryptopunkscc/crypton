package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Dependency

inline fun <reified T> T.dep(any: Any = T::class.java) = Dependency(this, depKey(any))

inline fun <reified T> depKey(any: Any = T::class.java): Dependency.Key<T> = Dependency.Key(any)
