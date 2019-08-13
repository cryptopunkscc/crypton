package cc.cryptopunks.crypton.app.app

import cc.cryptopunks.kache.core.Kache
import cc.cryptopunks.crypton.app.module.ApplicationModule
import cc.cryptopunks.crypton.app.module.CoreModule
import cc.cryptopunks.crypton.app.ui.component.FragmentComponent
import cc.cryptopunks.crypton.app.ui.component.GraphComponent
import cc.cryptopunks.crypton.app.util.ActivityStack
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        CoreModule::class
    ]
)
interface AppComponent : (App) -> App {

    val provideKache: Kache.Provider
    val activityStackCache: ActivityStack.Cache

    fun graphComponent(): GraphComponent.Builder
    fun contextComponent(): ContextComponent.Builder
    fun fragmentComponent(): FragmentComponent.Builder
}