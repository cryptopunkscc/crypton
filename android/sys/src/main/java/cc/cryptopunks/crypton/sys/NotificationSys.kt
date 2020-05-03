package cc.cryptopunks.crypton.sys

import android.app.NotificationManager
import cc.cryptopunks.crypton.context.Notification
import kotlin.reflect.KClass

internal class NotificationSys(
    private val notificationManager: NotificationManager,
    private val notificationFactories: Map<KClass<out Notification>, (Notification) -> android.app.Notification>
) : Notification.Sys {

    override fun show(notification: Notification) = notificationManager.notify(
        notification.id,
        notificationFactories.getOrElse(notification::class) {
            throw IllegalArgumentException("Missing notification factory for ${notification::class}")
        }(notification)
    )

    override fun cancel(notification: Notification) {
        notificationManager.cancel(notification.id)
    }
}
