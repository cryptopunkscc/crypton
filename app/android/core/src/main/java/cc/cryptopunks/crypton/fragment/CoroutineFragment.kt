package cc.cryptopunks.crypton.fragment

import androidx.annotation.CallSuper
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

abstract class CoroutineFragment :
    BaseFragment(),
    CoroutineScope {

    override val coroutineContext = SupervisorJob() +
        Dispatchers.Main +
        CoroutineLog.Label(javaClass.simpleName) +
        CoroutineExceptionHandler {
                coroutineContext,
                throwable,
            ->
            log.e { this.throwable = throwable }
            onException(coroutineContext, throwable)
        }

    open fun onException(coroutineContext: CoroutineContext, throwable: Throwable) = Unit

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
