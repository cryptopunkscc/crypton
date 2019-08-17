package cc.cryptopunks.crypton.app.presentation.fragment

import cc.cryptopunks.crypton.app.presentation.component.DaggerViewModelComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.util.BaseFragment

abstract class AppFragment : BaseFragment() {

    val viewModelComponent by lazy {
        DaggerViewModelComponent
            .builder()
            .graphComponent(baseActivity.graphComponent)
            .viewModelModule(ViewModelModule())
            .build()
    }
}