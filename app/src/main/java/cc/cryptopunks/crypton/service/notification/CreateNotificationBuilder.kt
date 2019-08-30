package cc.cryptopunks.crypton.service.notification

import android.app.Notification.Builder
import android.app.Service
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N_MR1
import cc.cryptopunks.crypton.module.ServiceScope
import cc.cryptopunks.crypton.service.AppService
import javax.inject.Inject

@ServiceScope
class CreateNotificationBuilder @Inject constructor(
    service: Service
) : () -> Builder by {
    when {
        SDK_INT > N_MR1 -> Builder(
            service,
            AppService.NOTIFICATION_CHANNEL_ID
        )
        else -> Builder(service)
    }
}