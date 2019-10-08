package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.feature.account.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.notification.ShowAppServiceNotification
import dagger.Component

@Component(
    dependencies = [ServiceComponent::class]
)
internal interface IndicatorComponent {
    val reconnectAccounts: ReconnectAccountsInteractor
    val setupNotificationChannel: SetupNotificationChannel
    val showAppServiceNotification: ShowAppServiceNotification
}