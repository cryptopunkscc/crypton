package cc.cryptopunks.crypton.activity

import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.typedLog
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

abstract class FeatureActivity :
    BaseActivity(),
    CoroutineScope {

    private val log = typedLog().builder

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

    val key: Any get() = javaClass.name

    override fun onStop() {
        coroutineContext.cancelChildren(Stop)
        super.onStop()
    }

    override fun onDestroy() {
        cancel(Destroy)
        super.onDestroy()
    }

    object Stop : CancellationException()
    object Destroy : CancellationException()
}
