package cc.cryptopunks.crypton.widget

import android.content.Context
import android.widget.FrameLayout
import cc.cryptopunks.crypton.context.Actor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ActorLayout(
    context: Context
) :
    FrameLayout(context),
    Actor {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}
