package cc.cryptopunks.crypton.service.notification

import cc.cryptopunks.crypton.dagger.ServiceScope
import javax.inject.Inject

@ServiceScope
class ShowAppServiceNotification @Inject constructor(
    createNotificationBuilder: CreateNotificationBuilder,
    setupNotificationBuilder: SetupNotificationBuilder,
    showNotification: ShowNotification
) : () -> Unit by {

    showNotification(setupNotificationBuilder(createNotificationBuilder()).build())
}