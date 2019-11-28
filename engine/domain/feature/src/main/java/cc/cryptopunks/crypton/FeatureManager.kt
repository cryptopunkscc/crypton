package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.core.FeatureCore
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class FeatureManager(
    private val createFeatureCore: FeatureCore.Create
) {

    private val requests = mutableMapOf<Any, AtomicInteger>()
    private val map = WeakHashMap<Any, FeatureCore>()

    fun request(key: Any): FeatureCore = synchronized(this) {
        map.getOrPut(key) {
            requests[key] = AtomicInteger()
            createFeatureCore()
        }.also {
            requests[key]!!.incrementAndGet()
        }
    }

    fun release(key: Any): Unit = synchronized(this) {
        if (requests[key]!!.decrementAndGet() == 0)
            map.remove(key)
    }

    interface Core {
        val createFeatureCore: FeatureCore.Create
        val featureManager: FeatureManager
    }
}