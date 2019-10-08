package cc.cryptopunks.crypton.module

import android.app.NotificationManager
import android.app.Service
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.component.ContextComponent
import cc.cryptopunks.crypton.component.ServiceComponent

class ServiceModule(
    override val service: Service
) :
    ServiceComponent,
    ContextComponent {

    override val context get() = service
    override val notificationManager: NotificationManager get() = service.getSystemService()!!
}