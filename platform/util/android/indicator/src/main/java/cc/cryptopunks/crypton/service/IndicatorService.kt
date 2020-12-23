package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.context.mainClass
import cc.cryptopunks.crypton.notification.CreateNotificationChannel
import cc.cryptopunks.crypton.notification.CreateNotificationChannel.Importance
import cc.cryptopunks.crypton.notification.ShowForegroundNotification
import cc.cryptopunks.crypton.notification.ShowIndicatorNotification

class IndicatorService : IntentService(Indicator.serviceName) {

    private val createNotificationChannel by lazy {
        CreateNotificationChannel(getSystemService()!!)
    }
    private val showIndicatorNotification by lazy {
        ShowIndicatorNotification(
            mainActivityClass = (application as RootScope).mainClass.type,
            context = this,
            showNotification = ShowForegroundNotification(
                service = this,
                notificationManager = getSystemService()!!
            ),
            smallIconResId = smallIconResId,
            appNameResId = appNameResId
        )
    }

    override fun onCreate() {
        isRunning = true
        super.onCreate()
        createNotificationChannel(
            id = Indicator.Notification.channelId,
            importance = Importance.Min
        )
        createNotificationChannel(
            id = Message.Notification.channelId,
            importance = Importance.High
        )
        showIndicatorNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        Service.START_STICKY

    override fun onHandleIntent(intent: Intent?) = Unit /*no-op*/

    companion object {
        var appNameResId: Int = 0
        var smallIconResId: Int = 0

        var isRunning = false
            private set
    }
}
