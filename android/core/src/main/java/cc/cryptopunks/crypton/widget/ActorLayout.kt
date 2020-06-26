package cc.cryptopunks.crypton.widget

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import cc.cryptopunks.crypton.context.Actor
import kotlinx.android.extensions.LayoutContainer
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

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
