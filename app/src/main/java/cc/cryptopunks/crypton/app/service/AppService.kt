package cc.cryptopunks.crypton.app.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import cc.cryptopunks.crypton.app.presentation.component.DaggerAppServiceComponent
import cc.cryptopunks.crypton.app.presentation.component.AppServiceComponent
import cc.cryptopunks.crypton.app.service.notification.SetupNotificationChannel
import cc.cryptopunks.crypton.app.service.notification.ShowAppServiceNotification
import cc.cryptopunks.crypton.core.app
import cc.cryptopunks.crypton.core.component.DaggerContextComponent
import cc.cryptopunks.crypton.account.domain.command.ReconnectAccounts
import cc.cryptopunks.crypton.core.module.ContextModule
import cc.cryptopunks.crypton.core.module.ServiceModule
import cc.cryptopunks.crypton.core.module.ServiceScope
import cc.cryptopunks.crypton.core.util.AsyncExecutor
import cc.cryptopunks.crypton.core.util.DisposableDelegate
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
        async: AsyncExecutor,
        reconnectAccounts: ReconnectAccounts,
        setupNotificationChannel: SetupNotificationChannel,
        showAppServiceNotification: ShowAppServiceNotification
    ) {
        setupNotificationChannel()
        showAppServiceNotification()
        async(task = reconnectAccounts)
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