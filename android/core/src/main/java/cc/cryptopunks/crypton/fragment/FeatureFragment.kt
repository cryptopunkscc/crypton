package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.activity.FeatureActivity
import kotlinx.coroutines.CoroutineScope

abstract class FeatureFragment :
    CoroutineFragment(),
    CoroutineScope {

    private val featureActivity get() = activity as FeatureActivity
    private val key by lazy { featureActivity.key }

    val appCore get() = featureActivity.appCore

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}
