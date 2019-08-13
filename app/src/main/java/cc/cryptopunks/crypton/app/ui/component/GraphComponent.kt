package cc.cryptopunks.crypton.app.ui.component

import cc.cryptopunks.crypton.app.module.GraphModule
import cc.cryptopunks.crypton.app.module.GraphScope
import cc.cryptopunks.crypton.app.util.Navigation
import dagger.Subcomponent

@GraphScope
@Subcomponent(
    modules = [GraphModule::class]
)
interface GraphComponent {

    val navigationBus: Navigation.Bus

    fun viewModelComponent() : ViewModelComponent.Builder

    @Subcomponent.Builder
    interface Builder {
        fun build(): GraphComponent
    }
}