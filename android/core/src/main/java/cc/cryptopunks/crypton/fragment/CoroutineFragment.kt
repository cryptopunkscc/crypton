package cc.cryptopunks.crypton.fragment

import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

abstract class CoroutineFragment :
    BaseFragment(),
    CoroutineScope {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main + CoroutineLog.Label(javaClass.simpleName)

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        cancel(Destroy(this))
    }

    class Destroy(message: String) : CancellationException(message) {
        constructor(any: Any) : this(any.javaClass.name)
    }
}
