package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.dagger.DaggerContextComponent
import cc.cryptopunks.crypton.domain.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.dagger.DaggerContextModule
import cc.cryptopunks.crypton.dagger.DaggerServiceModule
import cc.cryptopunks.crypton.dagger.ServiceScope
import cc.cryptopunks.crypton.presentation.component.AppServiceComponent
import cc.cryptopunks.crypton.presentation.component.DaggerAppServiceComponent
import cc.cryptopunks.crypton.service.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.service.notification.ShowAppServiceNotification
import timber.log.Timber
import javax.inject.Inject

@ServiceScope
class AppService :
    IntentService("AppService") {

    private val component: AppServiceComponent by lazy {
        DaggerAppServiceComponent
            .builder()
            .contextComponent(
                DaggerContextComponent
                    .builder()
                    .daggerContextModule(DaggerContextModule(this))
                    .component(app.component)
                    .build()
            )
            .daggerServiceModule(DaggerServiceModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }

    @Inject
    fun init(
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