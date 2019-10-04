package cc.cryptopunks.crypton.presentation.component

import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DaggerViewModelModule
import cc.cryptopunks.crypton.dagger.ViewModelScope
import cc.cryptopunks.crypton.presentation.fragment.DashboardFragment
import cc.cryptopunks.crypton.presentation.fragment.MainFragment
import dagger.Component

@ViewModelScope
@Component(
    modules = [
        DaggerFeatureModule::class,
        DaggerViewModelModule::class
    ]
)
interface AppViewModelComponent : ViewModelComponent {
    fun inject(target: MainFragment)
    fun inject(target: DashboardFragment)
}