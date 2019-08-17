package cc.cryptopunks.crypton.app.service.notification

import android.app.Notification.Builder
import android.app.Service
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.N_MR1
import cc.cryptopunks.crypton.core.module.ServiceScope
import cc.cryptopunks.crypton.app.service.XmppService
import javax.inject.Inject

@ServiceScope
class CreateNotificationBuilder @Inject constructor(
    service: Service
) : () -> Builder by {
    when {
        SDK_INT > N_MR1 -> Builder(
            service,
            XmppService.NOTIFICATION_CHANNEL_ID
        )
        else -> Builder(service)
    }
}