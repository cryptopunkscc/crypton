package cc.cryptopunks.crypton.widget

import android.content.Context
import android.widget.FrameLayout
import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.typedLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ConnectableLayout(
    context: Context
) :
    FrameLayout(context),
    Connectable {

    private val log = typedLog().builder

    override val coroutineContext = SupervisorJob() +
        Dispatchers.Main +
        CoroutineLog.Label(javaClass.simpleName) +
        CoroutineExceptionHandler { _, throwable ->
            log.e { this.throwable = throwable }
        }

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
