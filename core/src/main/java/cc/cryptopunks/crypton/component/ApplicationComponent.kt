package cc.cryptopunks.crypton.component

import android.app.Application
import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.module.ApplicationModule
import cc.cryptopunks.crypton.module.DataModule
import cc.cryptopunks.crypton.module.KacheModule
import cc.cryptopunks.crypton.util.BroadcastError
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    KacheModule::class,
    BroadcastError.Module::class,
    DataModule::class
])
interface ApplicationComponent :
    UtilsComponent,
    DataComponent,
    RepoComponent,
    DomainComponent,
    KacheComponent,
    BroadcastError.Component,
    Client.Component {

    val application: Application
}