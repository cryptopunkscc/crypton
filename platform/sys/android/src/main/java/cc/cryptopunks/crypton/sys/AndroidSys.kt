package cc.cryptopunks.crypton.sys

import android.app.Application
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.context.Clip
import cc.cryptopunks.crypton.context.Device
import cc.cryptopunks.crypton.context.Execute
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Network
import cc.cryptopunks.crypton.context.Notification
import cc.cryptopunks.crypton.context.Sys
import cc.cryptopunks.crypton.context.URI
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
        AndroidNetworkSysV2(
            scope = GlobalScope,
            connectivity = application.getSystemService()!!
        )
    }

    override val deviceSys: Device.Sys by lazy {
        DeviceSys(
            context = application
        )
    }

    override val executeSys: Execute.Sys get() = ExecuteSys

    override val uriSys: URI.Sys by lazy {
        AndroidUriSys(
            context = application
        )
    }
}
