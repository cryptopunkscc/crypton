package cc.cryptopunks.crypton.core.component

import android.app.Application
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.core.module.ApplicationModule
import cc.cryptopunks.crypton.core.module.DatabaseModule
import cc.cryptopunks.crypton.core.module.KacheModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    KacheModule::class,
    DatabaseModule::class
])
interface ApplicationComponent :
    UtilsComponent,
    DaoComponent,
    KacheComponent,
    Client.Component {

    val application: Application
}