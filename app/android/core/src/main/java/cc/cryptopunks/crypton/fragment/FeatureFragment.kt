package cc.cryptopunks.crypton.fragment

import android.content.Context
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.activity.FeatureActivity
import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class FeatureFragment :
    BaseFragment(),
    CoroutineScope {

    private val featureActivity get() = activity as FeatureActivity
    private val key by lazy { featureActivity.key }

    val rootScope get() = featureActivity.rootScope

    override val coroutineContext by lazy {
        rootScope.coroutineContext +
            SupervisorJob() +
            Dispatchers.Main +
            CoroutineLog.Label(javaClass.simpleName) +
            CoroutineExceptionHandler {
                    coroutineContext,
                    throwable,
                ->
                log.e { this.throwable = throwable }
                onException(coroutineContext, throwable)
            } +
            dep<Fragment>()
    }

    open fun onException(coroutineContext: CoroutineContext, throwable: Throwable) = Unit

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (view as? CoroutineScope)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    fun CoroutineScope.fragmentScope() = CoroutineScope(coroutineContext + this@FeatureFragment.dep<Fragment>())
}

val Context.applicationScope get() = applicationContext as CoroutineScope

val CoroutineScope.fragment: Fragment by dep()

interface FragmentScope : Scope {
    val fragment: Fragment
}
