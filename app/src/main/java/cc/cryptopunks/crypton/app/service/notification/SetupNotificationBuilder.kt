package cc.cryptopunks.crypton.app.service.notification

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.module.ServiceScope
import cc.cryptopunks.crypton.app.ui.activity.InitialActivity
import javax.inject.Inject

@ServiceScope
class SetupNotificationBuilder @Inject constructor(
    private val service: Service
) : (Notification.Builder) -> Notification.Builder by { builder ->

    builder
        .setContentTitle(service.getText(R.string.app_name))
        .setContentText("Content text")
        .setSmallIcon(R.mipmap.ic_launcher_round)
        .setContentIntent(
            PendingIntent.getActivity(
                service,
                REQUEST_CODE,
                Intent(service, InitialActivity::class.java),
                FLAGS
            )
        )!!
} {
    private companion object {
        const val REQUEST_CODE = 0
        const val FLAGS = 0
    }
}