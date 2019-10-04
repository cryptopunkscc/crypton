package cc.cryptopunks.crypton.presentation.component

import cc.cryptopunks.crypton.service.AppService
import cc.cryptopunks.crypton.dagger.ContextComponent
import cc.cryptopunks.crypton.dagger.DaggerServiceModule
import cc.cryptopunks.crypton.dagger.ServiceScope
import dagger.Component

@ServiceScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [DaggerServiceModule::class]
)
interface AppServiceComponent : ContextComponent {

    fun inject(appService: AppService)
}