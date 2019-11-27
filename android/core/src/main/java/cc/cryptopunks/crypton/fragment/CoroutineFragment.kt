package cc.cryptopunks.crypton.fragment

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment :
    BaseFragment(),
    CoroutineScope {

    private val scope = MainScope()

    override val coroutineContext: CoroutineContext get() = scope.coroutineContext

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}