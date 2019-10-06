package cc.cryptopunks.crypton.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import cc.cryptopunks.crypton.service.IndicatorService
import javax.inject.Inject


internal class SetupNotificationChannel @Inject constructor(
    private val notificationManager: NotificationManager
) : () -> Unit by {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                IndicatorService.NOTIFICATION_CHANNEL_ID,
                IndicatorService.TAG,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
}