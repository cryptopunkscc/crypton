package cc.cryptopunks.crypton.module

import android.app.NotificationManager
import android.app.Service
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.androidCore
import cc.cryptopunks.crypton.component.AndroidCore
import cc.cryptopunks.crypton.component.ServiceComponent

class ServiceModule(
    override val service: Service
) :
    ServiceComponent,
    AndroidCore by androidCore {

    override val context get() = service
    override val notificationManager: NotificationManager get() = service.getSystemService()!!
}