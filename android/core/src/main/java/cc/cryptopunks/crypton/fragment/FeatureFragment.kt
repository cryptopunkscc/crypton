package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.activity.FeatureActivity

abstract class FeatureFragment :
    CoroutineFragment() {

    private val featureActivity get() = activity as FeatureActivity
    private val key by lazy { featureActivity.key }

    val appScope get() = featureActivity.appScope

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}
