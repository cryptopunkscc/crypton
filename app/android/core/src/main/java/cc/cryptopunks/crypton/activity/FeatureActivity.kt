package cc.cryptopunks.crypton.activity

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren

abstract class FeatureActivity : BaseActivity() {

    private val scope = MainScope()

    val key: Any get() = javaClass.name

    override fun onStop() {
        scope.coroutineContext.cancelChildren(Stop)
        super.onStop()
    }

    override fun onDestroy() {
        scope.cancel(Destroy)
        super.onDestroy()
    }

    object Stop : CancellationException()
    object Destroy : CancellationException()
}
