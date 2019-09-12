package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.presentation.component.AppViewModelComponent
import cc.cryptopunks.crypton.presentation.component.DaggerAppViewModelComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseAppFragment : BaseFragment() {

    val viewModelComponent: AppViewModelComponent by lazy {
        DaggerAppViewModelComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .build()!!
    }
}