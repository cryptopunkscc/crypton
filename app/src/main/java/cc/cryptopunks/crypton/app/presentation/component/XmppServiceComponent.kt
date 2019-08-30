package cc.cryptopunks.crypton.app.presentation.component

import cc.cryptopunks.crypton.app.service.AppService
import cc.cryptopunks.crypton.component.ContextComponent
import cc.cryptopunks.crypton.module.ServiceModule
import cc.cryptopunks.crypton.module.ServiceScope
import dagger.Component

@ServiceScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ServiceModule::class]
)
interface AppServiceComponent : ContextComponent {

    fun inject(appService: AppService)
}