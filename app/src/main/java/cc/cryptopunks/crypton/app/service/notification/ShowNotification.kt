package cc.cryptopunks.crypton.app.service.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N_MR1
import cc.cryptopunks.crypton.core.module.ServiceScope
import cc.cryptopunks.crypton.app.service.AppService.Companion.NOTIFICATION_ID
import javax.inject.Inject

@ServiceScope
class ShowNotification @Inject constructor(
    private val service: Service,
    private val notificationManager: NotificationManager
) : (Notification) -> Unit by { notification ->

    when {
        SDK_INT > N_MR1 -> service.startForeground(NOTIFICATION_ID, notification)
        else -> notificationManager.notify(NOTIFICATION_ID, notification)
    }
}