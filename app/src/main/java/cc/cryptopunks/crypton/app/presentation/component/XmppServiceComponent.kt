package cc.cryptopunks.crypton.app.presentation.component

import cc.cryptopunks.crypton.app.service.AppService
import cc.cryptopunks.crypton.core.component.ContextComponent
import cc.cryptopunks.crypton.core.module.ServiceModule
import cc.cryptopunks.crypton.core.module.ServiceScope
import dagger.Component

@ServiceScope
@Component(
    dependencies = [ContextComponent::class],
    modules = [ServiceModule::class]
)
interface AppServiceComponent : ContextComponent {

    fun inject(appService: AppService)
}