package cc.cryptopunks.crypton.app

import cc.cryptopunks.crypton.app.module.ActivityModule
import cc.cryptopunks.crypton.app.module.ContextModule
import cc.cryptopunks.crypton.app.module.ContextScope
import cc.cryptopunks.crypton.app.module.ServiceModule
import cc.cryptopunks.crypton.app.service.ServiceComponent
import cc.cryptopunks.crypton.app.ui.component.ActivityComponent
import dagger.Subcomponent

@ContextScope
@Subcomponent(modules = [ContextModule::class])
interface ContextComponent {

    fun service(module: ServiceModule): ServiceComponent
    fun activity(module: ActivityModule): ActivityComponent

    @Subcomponent.Builder
    interface Builder {
        fun plus(module: ContextModule): Builder
        fun build(): ContextComponent
    }
}