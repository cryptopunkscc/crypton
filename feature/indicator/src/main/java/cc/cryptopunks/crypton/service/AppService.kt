package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.dagger.DaggerContextModule
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DaggerIndicatorComponent
import cc.cryptopunks.crypton.dagger.IndicatorComponent
import cc.cryptopunks.crypton.domain.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.service.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.service.notification.ShowAppServiceNotification
import timber.log.Timber
import javax.inject.Inject

class AppService :
    IntentService("AppService") {

    private val component: IndicatorComponent by lazy {
        DaggerIndicatorComponent.builder()
            .daggerContextModule(DaggerContextModule(this))
            .daggerFeatureModule(DaggerFeatureModule(app.component.featureComponent()))
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
        val TAG: String = AppService::class.java.simpleName
        val NOTIFICATION_ID = TAG.hashCode()
        val NOTIFICATION_CHANNEL_ID = TAG + "_notification_channel_id"
    }
}