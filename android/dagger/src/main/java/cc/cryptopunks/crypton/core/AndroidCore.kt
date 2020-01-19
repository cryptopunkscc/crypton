package cc.cryptopunks.crypton.core

import android.app.Application
import android.app.NotificationManager
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.FeatureManager
import cc.cryptopunks.crypton.context.AndroidService
import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.intent.IntentProcessor
import cc.cryptopunks.crypton.interactor.DisconnectAccountsInteractor
import cc.cryptopunks.crypton.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.manager.AccountManager
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.notification.NotifyUnreadMessages
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.service.AppServices
import cc.cryptopunks.crypton.service.ServiceManager
import cc.cryptopunks.crypton.sys.GetNetworkStatus
import cc.cryptopunks.crypton.sys.SetToClipboard
import cc.cryptopunks.crypton.sys.StartIndicatorService
import cc.cryptopunks.crypton.sys.StopIndicatorService
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.IOExecutor
import cc.cryptopunks.crypton.util.MainExecutor
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        Application::class,
        Repo::class
    ], modules = [
        AndroidCore.Module::class,
        AndroidCore.Bindings::class,
        AndroidFeatureCoreFactory.Bindings::class,
        AndroidServiceCoreFactory.Bindings::class
    ]
)
interface AndroidCore :
    Api.Core,
    FeatureManager.Core,
    SessionManager.Core,
    ServiceManager.Core,
    PresenceManager.Core,
    IntentProcessor.Core,
    AndroidService.Core.Factory.Core,
    AppServices {

    val application: Application
    val mainActivityClass: Class<*>

    val notificationManager: NotificationManager
    val connectivityManager: ConnectivityManager

    val accountManager: AccountManager

    val currentSession: CurrentSessionSelector
    val reconnectAccounts: ReconnectAccountsInteractor
    val disconnectAccounts: DisconnectAccountsInteractor


    @dagger.Module
    class Module(
        @get:Provides val mainActivityClass: Class<*>,
        @get:Provides val createConnection: Connection.Factory
    ) {
        @get:Provides
        val serviceScope = Service.Scope()

        @get:Provides
        val mainExecutor = MainExecutor(ArchTaskExecutor.getMainThreadExecutor())

        @get:Provides
        val ioExecutor = IOExecutor(ArchTaskExecutor.getIOThreadExecutor())

        @get:Provides
        val broadcastError = BroadcastError()

        @Provides
        fun Application.notificationManager(): NotificationManager = getSystemService()!!

        @Provides
        fun Application.connectivityManager(): ConnectivityManager = getSystemService()!!

        @Provides
        fun Application.clipboardManager(): ClipboardManager = getSystemService()!!

        @Provides
        @Singleton
        fun featureManager(create: FeatureCore.Create) = FeatureManager(create)
    }

    @dagger.Module
    interface Bindings {
        @Binds
        fun GetNetworkStatus.getStatus(): Network.Sys.GetStatus

        @Binds
        fun SetToClipboard.setClip(): Clip.Board.Sys.SetClip

        @Binds
        fun NotifyUnreadMessages.unread(): Message.Notify.Unread

        @Binds
        fun StartIndicatorService.start(): Indicator.Sys.Show

        @Binds
        fun StopIndicatorService.stop(): Indicator.Sys.Hide
    }
}