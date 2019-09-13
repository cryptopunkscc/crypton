package cc.cryptopunks.crypton.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.component.DaggerContextComponent
import cc.cryptopunks.crypton.domain.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.module.ContextModule
import cc.cryptopunks.crypton.module.ServiceModule
import cc.cryptopunks.crypton.module.ServiceScope
import cc.cryptopunks.crypton.presentation.component.AppServiceComponent
import cc.cryptopunks.crypton.presentation.component.DaggerAppServiceComponent
import cc.cryptopunks.crypton.service.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.service.notification.ShowAppServiceNotification
import cc.cryptopunks.crypton.util.DisposableDelegate
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@ServiceScope
class AppService :
    IntentService("AppService"),
    DisposableDelegate {

    override val disposable = CompositeDisposable()

    private val component: AppServiceComponent by lazy {
        DaggerAppServiceComponent
            .builder()
            .contextComponent(
                DaggerContextComponent
                    .builder()
                    .contextModule(ContextModule(this))
                    .applicationComponent(app.component)
                    .build()
            )
            .serviceModule(ServiceModule(this))
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