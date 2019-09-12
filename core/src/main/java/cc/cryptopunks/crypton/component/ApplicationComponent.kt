package cc.cryptopunks.crypton.component

import android.app.Application
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.module.ApplicationModule
import cc.cryptopunks.crypton.module.DataModule
import cc.cryptopunks.crypton.module.KacheModule
import cc.cryptopunks.crypton.util.Scopes
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    KacheModule::class,
    DataModule::class
])
interface ApplicationComponent :
    UtilsComponent,
    DaoComponent,
    KacheComponent,
    Client.Component {

    val application: Application
    val commandScope: Scopes.Command
}