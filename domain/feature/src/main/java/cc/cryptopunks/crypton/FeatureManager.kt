package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.annotation.ApplicationScope
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

@ApplicationScope
class FeatureManager @Inject constructor(
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

    interface Component {
        val featureManager: FeatureManager
    }
}