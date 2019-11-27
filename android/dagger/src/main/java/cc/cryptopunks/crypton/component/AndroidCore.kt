package cc.cryptopunks.crypton.component

import android.app.Application
import android.app.NotificationManager
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.entity.Clipboard
import cc.cryptopunks.crypton.entity.Network
import cc.cryptopunks.crypton.interactor.DisconnectAccountsInteractor
import cc.cryptopunks.crypton.interactor.ReconnectAccountsInteractor
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.sys.GetNetworkStatus
import cc.cryptopunks.crypton.sys.SetClip
import dagger.Binds
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        Core::class,
        Application::class
    ], modules = [
        AndroidCore.Module::class,
        AndroidCore.Bindings::class
    ]
)
interface AndroidCore :
    Core,
    SessionManager.Component,
    PresentationManager.Component,
    PresenceManager.Component {

    val core: Core
    val application: Application
    val mainActivityClass: Class<*>

    val notificationManager: NotificationManager
    val connectivityManager: ConnectivityManager

    val getNetworkStatus: Network.Sys.GetStatus
    val setClip: Clipboard.Sys.SetClip

    val currentSession: CurrentSessionSelector
    val reconnectAccounts: ReconnectAccountsInteractor
    val disconnectAccounts: DisconnectAccountsInteractor


    @dagger.Module
    class Module(
        @get:Provides val mainActivityClass: Class<*>
    ) {
        @Provides
        fun Application.notificationManager(): NotificationManager = getSystemService()!!
        @Provides
        fun Application.connectivityManager(): ConnectivityManager = getSystemService()!!
        @Provides
        fun Application.clipboardManager(): ClipboardManager = getSystemService()!!
    }

    @dagger.Module
    interface Bindings {
        @Binds
        fun GetNetworkStatus.getStatus(): Network.Sys.GetStatus
        @Binds
        fun SetClip.setClip(): Clipboard.Sys.SetClip
    }
}