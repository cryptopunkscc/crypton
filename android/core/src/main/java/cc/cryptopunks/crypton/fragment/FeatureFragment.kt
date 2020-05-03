package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.activity.FeatureActivity
import cc.cryptopunks.crypton.context.FeatureCore
import kotlinx.coroutines.CoroutineScope

abstract class FeatureFragment :
    CoroutineFragment(),
    CoroutineScope {

    private val featureActivity get() = activity as FeatureActivity
    private val key by lazy { featureActivity.key }
    private val featureManager by lazy { featureActivity.featureManager }

    val appCore get() = featureActivity.appCore
    val featureCore: FeatureCore by lazy { featureManager.request(key) }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroy() {
        super.onDestroy()
        featureManager.release(key)
    }
}
