package cc.cryptopunks.crypton.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.*
import android.os.Build
import cc.cryptopunks.crypton.notification.CreateNotificationChannel.Importance.*

class CreateNotificationChannel(
    private val notificationManager: NotificationManager
) {
    operator fun invoke(
        id: String,
        name: CharSequence = id,
        importance: Importance = Default
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importanceInt = when (importance) {
                Min -> IMPORTANCE_MIN
                Low -> IMPORTANCE_LOW
                Default -> IMPORTANCE_DEFAULT
                High -> IMPORTANCE_HIGH
            }
            notificationManager.createNotificationChannel(
                NotificationChannel(id, name, importanceInt)
            )
        }
    }

    enum class Importance { Min, Low, Default, High }
}
