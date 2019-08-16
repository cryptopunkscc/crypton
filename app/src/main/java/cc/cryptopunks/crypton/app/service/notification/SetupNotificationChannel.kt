package cc.cryptopunks.crypton.app.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import cc.cryptopunks.crypton.app.module.ContextScope
import cc.cryptopunks.crypton.app.service.XmppService
import javax.inject.Inject

@ContextScope
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