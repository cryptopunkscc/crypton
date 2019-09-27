package cc.cryptopunks.crypton.presentation.component

import cc.cryptopunks.crypton.component.FeatureComponent
import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.module.ViewModelModule
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.presentation.fragment.DashboardFragment
import cc.cryptopunks.crypton.presentation.fragment.MainFragment
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [FeatureComponent::class],
    modules = [ViewModelModule::class]
)
interface AppViewModelComponent : ViewModelComponent {
    fun inject(target: MainFragment)
    fun inject(target: DashboardFragment)
}