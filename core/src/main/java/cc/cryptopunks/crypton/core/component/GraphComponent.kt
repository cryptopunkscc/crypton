package cc.cryptopunks.crypton.core.component

import cc.cryptopunks.crypton.core.module.GraphModule
import cc.cryptopunks.crypton.core.module.GraphScope
import cc.cryptopunks.crypton.core.util.Navigation
import dagger.Component

@GraphScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [GraphModule::class]
)
interface GraphComponent : ApplicationComponent {
    val navigationBus: Navigation.Bus
}