package cc.cryptopunks.crypton.service.notification


import javax.inject.Inject

internal class ShowAppServiceNotification @Inject constructor(
    createNotificationBuilder: CreateNotificationBuilder,
    setupNotificationBuilder: SetupNotificationBuilder,
    showNotification: ShowNotification
) : () -> Unit by {

    showNotification(setupNotificationBuilder(createNotificationBuilder()).build())
}