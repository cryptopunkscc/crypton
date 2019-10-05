package cc.cryptopunks.crypton.dagger

import cc.cryptopunks.crypton.presentation.fragment.DashboardFragment
import dagger.Component

@Component(
    modules = [
        DaggerFeatureModule::class,
        DaggerViewModelModule::class
    ]
)
interface DashboardComponent {
    fun inject(target: DashboardFragment)
}