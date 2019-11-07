package cc.cryptopunks.crypton.component

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.getSystemService
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.manager.PresenceManager
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        Core::class,
        Application::class
    ], modules = [
        AndroidCore.Module::class
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

    val currentSession: CurrentSessionSelector

    @dagger.Module
    class Module(
        @get:Provides val mainActivityClass: Class<*>
    ) {
        @Provides
        fun Application.notificationManager(): NotificationManager = getSystemService()!!
    }
}