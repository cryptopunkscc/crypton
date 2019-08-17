package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.AppComponent
import cc.cryptopunks.crypton.app.module.GraphModule
import cc.cryptopunks.crypton.app.module.GraphScope
import cc.cryptopunks.crypton.app.util.Navigation
import dagger.Component

@GraphScope
@Component(
    dependencies = [AppComponent::class],
    modules = [GraphModule::class]
)
interface GraphComponent : AppComponent {

    val navigationBus: Navigation.Bus
}