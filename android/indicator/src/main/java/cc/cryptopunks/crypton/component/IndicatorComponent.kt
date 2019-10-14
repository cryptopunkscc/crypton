package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.feature.main.service.UpdateCurrentClientService
import cc.cryptopunks.crypton.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.notification.ShowAppServiceNotification
import dagger.Component

@Component(
    dependencies = [ServiceComponent::class]
)
internal interface IndicatorComponent {
    val setupNotificationChannel: SetupNotificationChannel
    val showAppServiceNotification: ShowAppServiceNotification
    val updateCurrentClientService: UpdateCurrentClientService
}