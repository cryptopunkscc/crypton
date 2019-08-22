package cc.cryptopunks.crypton.core.component

import android.app.Application
import cc.cryptopunks.crypton.core.module.ApplicationModule
import cc.cryptopunks.crypton.core.util.ActivityStack
import cc.cryptopunks.crypton.core.util.AsyncExecutor
import cc.cryptopunks.crypton.common.HandleError
import cc.cryptopunks.crypton.core.module.DatabaseModule
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.kache.core.Kache
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    DatabaseModule::class
])
interface ApplicationComponent : DaoComponent {

    val application: Application
    val provideKache: Kache.Provider
    val activityStackCache: ActivityStack.Cache
    val executeAsync: AsyncExecutor
    val createClient: Client.Factory
    val clientCache: Client.Cache
    val handleError: HandleError
    val errorPublisher: HandleError.Publisher
}