package cc.cryptopunks.crypton.sys

import android.app.Application
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.GlobalScope
import kotlin.reflect.KClass

class AndroidSys(
    application: Application,
    notificationFactories: Map<KClass<out Notification>, (Notification) -> android.app.Notification>
) : Sys {

    override val indicatorSys: Indicator.Sys by lazy {
        IndicatorSys(application)
    }

    override val notificationSys: Notification.Sys by lazy {
        NotificationSys(
            notificationManager = application.getSystemService()!!,
            notificationFactories = notificationFactories
        )
    }

    override val clipboardSys: Clip.Board.Sys by lazy {
        ClipBoardSys(
            clipboard = application.getSystemService()!!
        )
    }

    override val networkSys: Network.Sys by lazy {
        NetworkSys(
            scope = GlobalScope,
            connectivityManager = application.getSystemService()!!
        )
    }
}
