package cc.cryptopunks.crypton.app.presentation.component

import cc.cryptopunks.crypton.app.presentation.fragment.*
import cc.cryptopunks.crypton.core.component.GraphComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.api.Client
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [GraphComponent::class],
    modules = [
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface ViewModelComponent : GraphComponent {
    fun inject(target: NavigationFragment)
    fun inject(target: ChatListFragment)
}