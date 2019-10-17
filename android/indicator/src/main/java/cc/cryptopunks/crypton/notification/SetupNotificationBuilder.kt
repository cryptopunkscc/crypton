package cc.cryptopunks.crypton.notification

import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.indicator.R
import javax.inject.Inject

internal class SetupNotificationBuilder @Inject constructor(
    private val service: Service,
    private val mainActivityClass: Class<out Activity>
) : (Notification.Builder) -> Notification.Builder by { builder ->

    builder
        .setContentTitle(service.getText(R.string.app_name))
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentIntent(
            PendingIntent.getActivity(
                service,
                REQUEST_CODE,
                Intent(service, mainActivityClass),
                FLAGS
            )
        )!!
} {
    private companion object {
        const val REQUEST_CODE = 0
        const val FLAGS = 0
    }
}