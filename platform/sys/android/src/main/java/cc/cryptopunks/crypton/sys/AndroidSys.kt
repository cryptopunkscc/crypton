package cc.cryptopunks.crypton.sys

import android.app.Application
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.service.IndicatorService
import kotlinx.coroutines.GlobalScope
import kotlin.reflect.KClass

class AndroidSys(
    application: Application,
    notificationFactories: Map<KClass<out Notification>, (Notification) -> android.app.Notification>,
    appNameResId: Int = 0,
    smallIconResId: Int = 0
) : Sys {

    override val indicatorSys: Indicator.Sys by lazy {
        IndicatorService.appNameResId = appNameResId
        IndicatorService.smallIconResId = smallIconResId
        IndicatorSys(
            context = application
        )
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

    override val deviceSys: Device.Sys by lazy {
        DeviceSys(
            context = application
        )
    }
}
