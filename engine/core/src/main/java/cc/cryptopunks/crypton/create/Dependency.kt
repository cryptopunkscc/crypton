package cc.cryptopunks.crypton.create

import cc.cryptopunks.crypton.Dependency
import java.lang.ref.WeakReference

inline fun <reified T> T.dep(any: Any = T::class.java):
    Dependency<T> =
    Dependency(depKey(any)) { this }

inline fun <reified T> T.weakDep(any: Any = T::class.java):
    Dependency<T> =
    WeakReference(this).run { Dependency(depKey(any)) { get()!! } }

inline fun <reified T> depKey(any: Any = T::class.java):
    Dependency.Key<T> =
    Dependency.Key(any)
