package cc.cryptopunks.crypton.fragment

import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class CoroutineFragment :
    BaseFragment(),
    CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}