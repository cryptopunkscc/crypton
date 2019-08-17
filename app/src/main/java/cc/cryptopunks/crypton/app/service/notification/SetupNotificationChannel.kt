package cc.cryptopunks.crypton.app.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import cc.cryptopunks.crypton.app.service.XmppService
import cc.cryptopunks.crypton.core.module.ServiceScope
import javax.inject.Inject

@ServiceScope
class SetupNotificationChannel @Inject constructor(
    private val notificationManager: NotificationManager
) : () -> Unit by {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        notificationManager.createNotificationChannel(
            NotificationChannel(
                XmppService.NOTIFICATION_CHANNEL_ID,
                XmppService.TAG,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
}