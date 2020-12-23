package cc.cryptopunks.crypton.widget

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.typedLog
import kotlinx.android.extensions.LayoutContainer
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ActorLayoutContainer(
    override val containerView: View
) : LayoutContainer,
    Actor {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}

abstract class ActorLayout(
    context: Context
) :
    FrameLayout(context),
    Actor {

    private val log = typedLog().builder

    override val coroutineContext = SupervisorJob() +
        Dispatchers.Main +
        CoroutineLog.Label(javaClass.simpleName) +
        CoroutineExceptionHandler { _, throwable ->
            log.e { this.throwable = throwable }
        }

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
