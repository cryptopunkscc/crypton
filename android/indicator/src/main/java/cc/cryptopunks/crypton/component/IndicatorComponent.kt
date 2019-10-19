package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.notification.CreateNotificationChannel
import cc.cryptopunks.crypton.notification.ShowIndicatorNotification
import dagger.Component

@Component(
    dependencies = [ServiceComponent::class]
)
internal interface IndicatorComponent {
    val createNotificationChannel: CreateNotificationChannel
    val showIndicatorNotification: ShowIndicatorNotification
}