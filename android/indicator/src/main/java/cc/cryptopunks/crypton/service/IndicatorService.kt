package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.Engine
import cc.cryptopunks.crypton.ServiceCore
import cc.cryptopunks.crypton.context.Indicator
import cc.cryptopunks.crypton.notification.CreateNotificationChannel
import cc.cryptopunks.crypton.notification.CreateNotificationChannel.Importance.Min
import cc.cryptopunks.crypton.notification.ShowIndicatorNotification
import cc.cryptopunks.crypton.util.ext.resolve

class IndicatorService : IntentService(Indicator.serviceName) {

    interface Core {
        val createNotificationChannel: CreateNotificationChannel
        val showIndicatorNotification: ShowIndicatorNotification
    }

    private val core
        get() = this
            .application.resolve<Engine>()
            .core.resolve<ServiceCore.Factory.Core>()
            .createServiceCore(this).resolve<Core>()

    override fun onCreate() {
        super.onCreate()
        core.run {
            createNotificationChannel(
                id = Indicator.Notification.channelId,
                importance = Min
            )
            showIndicatorNotification()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        Service.START_STICKY

    override fun onHandleIntent(intent: Intent?) = Unit /*no-op*/
}