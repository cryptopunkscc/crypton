package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.component.DaggerIndicatorComponent
import cc.cryptopunks.crypton.component.IndicatorComponent
import cc.cryptopunks.crypton.module.ServiceModule
import timber.log.Timber

class IndicatorService :
    IntentService("IndicatorService") {

    private val component: IndicatorComponent by lazy {
        DaggerIndicatorComponent.builder()
            .serviceComponent(ServiceModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.run {
            setupNotificationChannel()
            showAppServiceNotification()
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        /*no-op*/
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = Service
        .START_STICKY
        .also { Timber.d("onStartCommand") }

    companion object {
        val TAG: String = IndicatorService::class.java.simpleName
        val NOTIFICATION_ID = TAG.hashCode()
        val NOTIFICATION_CHANNEL_ID = TAG + "_notification_channel_id"
    }
}