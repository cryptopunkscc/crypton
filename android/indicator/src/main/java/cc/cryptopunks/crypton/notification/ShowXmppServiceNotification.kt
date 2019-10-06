package cc.cryptopunks.crypton.notification

import javax.inject.Inject

internal class ShowAppServiceNotification @Inject constructor(
    createNotificationBuilder: CreateNotificationBuilder,
    setupNotificationBuilder: SetupNotificationBuilder,
    showNotification: ShowNotification
) : () -> Unit by {

    showNotification(setupNotificationBuilder(createNotificationBuilder()).build())
}