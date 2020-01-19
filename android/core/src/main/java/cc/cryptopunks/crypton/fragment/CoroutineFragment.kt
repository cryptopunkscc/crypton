package cc.cryptopunks.crypton.fragment

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class CoroutineFragment :
    BaseFragment(),
    CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}