package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DaggerViewModelModule
import cc.cryptopunks.crypton.module.ViewModelModule
import cc.cryptopunks.crypton.presentation.component.AppViewModelComponent
import cc.cryptopunks.crypton.presentation.component.DaggerAppViewModelComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseAppFragment : BaseFragment() {

    val viewModelComponent: AppViewModelComponent by lazy {
        DaggerAppViewModelComponent
            .builder()
            .daggerFeatureModule(DaggerFeatureModule(baseActivity.featureComponent))
            .daggerViewModelModule(DaggerViewModelModule(ViewModelModule(baseActivity.featureComponent)))
            .build()!!
    }
}