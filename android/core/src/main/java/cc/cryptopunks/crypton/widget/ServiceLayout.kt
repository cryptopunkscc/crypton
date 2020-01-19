package cc.cryptopunks.crypton.widget

import android.content.Context
import android.widget.FrameLayout
import cc.cryptopunks.crypton.context.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ServiceLayout(
    context: Context
) :
    FrameLayout(context),
    Service.Actor {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun toString() = javaClass.name + "@" + Integer.toHexString(hashCode())
}