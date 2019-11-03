package cc.cryptopunks.crypton.component

import android.app.Activity
import android.app.Application
import cc.cryptopunks.crypton.core.Core
import cc.cryptopunks.crypton.manager.SessionManager
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.selector.CurrentSessionSelector
import cc.cryptopunks.crypton.selector.SessionsSelector
import dagger.Component
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [
        Application::class,
        Core::class
    ], modules = [
        AndroidCore.Module::class
    ]
)
interface AndroidCore : Core {

    val core: Core
    val currentSession: CurrentSessionSelector
    val sessionsFlow: SessionsSelector
    val sessionManager: SessionManager
    val presentationManager: PresentationManager
    val application: Application
    val mainActivityClass: Class<out Activity>

    @dagger.Module
    class Module(
        @get:Provides val mainActivityClass: Class<out Activity>
    )
}