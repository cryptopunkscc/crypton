package cc.cryptopunks.crypton.presentation.dagger

import cc.cryptopunks.crypton.component.ViewModelComponent
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DaggerViewModelModule
import cc.cryptopunks.crypton.presentation.fragment.MainFragment
import dagger.Component

@Component(
    modules = [
        DaggerFeatureModule::class,
        DaggerViewModelModule::class
    ]
)
interface AppComponent : ViewModelComponent {
    fun inject(target: MainFragment)
}