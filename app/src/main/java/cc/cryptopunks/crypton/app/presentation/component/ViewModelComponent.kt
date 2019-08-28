package cc.cryptopunks.crypton.app.presentation.component

import cc.cryptopunks.crypton.app.presentation.fragment.*
import cc.cryptopunks.crypton.core.component.FeatureComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.api.Client
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [FeatureComponent::class],
    modules = [
        ViewModelModule::class,
        Client.Module::class
    ]
)
interface ViewModelComponent : FeatureComponent {
    fun inject(target: MainFragment)
    fun inject(target: DashboardFragment)
}