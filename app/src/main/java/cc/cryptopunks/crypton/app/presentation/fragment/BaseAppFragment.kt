package cc.cryptopunks.crypton.app.presentation.fragment

import cc.cryptopunks.crypton.app.presentation.component.DaggerViewModelComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class BaseAppFragment : BaseFragment() {

    val viewModelComponent by lazy {
        DaggerViewModelComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .build()!!
    }
}