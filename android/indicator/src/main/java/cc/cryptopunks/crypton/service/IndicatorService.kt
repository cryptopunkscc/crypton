package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.component.ServiceComponent
import cc.cryptopunks.crypton.entity.Indicator
import cc.cryptopunks.crypton.module.ServiceModule
import cc.cryptopunks.crypton.notification.CreateNotificationChannel
import cc.cryptopunks.crypton.notification.ShowIndicatorNotification

class IndicatorService : IntentService(Indicator.serviceName) {

    @dagger.Component(dependencies = [ServiceComponent::class])
    internal interface Component {
        val createNotificationChannel: CreateNotificationChannel
        val showIndicatorNotification: ShowIndicatorNotification
    }

    private val component: Component by lazy {
        DaggerIndicatorService_Component.builder()
            .serviceComponent(ServiceModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.run {
            createNotificationChannel(Indicator.Notification.channelId)
            showIndicatorNotification()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int =
        Service.START_STICKY

    override fun onHandleIntent(intent: Intent?) = Unit /*no-op*/
}