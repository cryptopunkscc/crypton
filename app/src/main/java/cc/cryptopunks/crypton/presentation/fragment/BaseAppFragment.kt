package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.presentation.component.DaggerViewModelComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseAppFragment : BaseFragment() {

    val viewModelComponent by lazy {
        DaggerViewModelComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .build()!!
    }
}