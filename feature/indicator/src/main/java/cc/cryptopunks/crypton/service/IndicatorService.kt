package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.component.DaggerIndicatorComponent
import cc.cryptopunks.crypton.component.IndicatorComponent
import cc.cryptopunks.crypton.domain.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.module.featureComponent
import cc.cryptopunks.crypton.module.serviceComponent
import cc.cryptopunks.crypton.service.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.service.notification.ShowAppServiceNotification
import timber.log.Timber
import javax.inject.Inject

class IndicatorService :
    IntentService("IndicatorService") {

    private val component: IndicatorComponent by lazy {
        DaggerIndicatorComponent.builder()
            .featureComponent(featureComponent())
            .serviceComponent(serviceComponent())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    @Inject
    internal fun init(
        reconnectAccounts: ReconnectAccountsInteractor,
        setupNotificationChannel: SetupNotificationChannel,
        showAppServiceNotification: ShowAppServiceNotification
    ) {
        setupNotificationChannel()
        showAppServiceNotification()
        reconnectAccounts()
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