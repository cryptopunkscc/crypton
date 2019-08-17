package cc.cryptopunks.crypton.app

import android.app.NotificationManager
import cc.cryptopunks.crypton.app.module.ContextModule
import cc.cryptopunks.crypton.app.module.ContextScope
import cc.cryptopunks.crypton.app.service.notification.SetupNotificationChannel
import dagger.Component

@ContextScope
@Component(
    dependencies = [AppComponent::class],
    modules = [ContextModule::class]
)
interface ContextComponent : AppComponent {

    val notificationManager: NotificationManager
    val setupNotificationChannel: SetupNotificationChannel
}