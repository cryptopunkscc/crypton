package cc.cryptopunks.crypton.account.presentation.fragment

import cc.cryptopunks.crypton.account.component.AccountComponent
import cc.cryptopunks.crypton.account.component.DaggerAccountComponent
import cc.cryptopunks.crypton.core.module.ViewModelModule
import cc.cryptopunks.crypton.core.util.BaseFragment

abstract class BaseAccountFragment : BaseFragment() {

    val accountComponent: AccountComponent by lazy {
        DaggerAccountComponent
            .builder()
            .graphComponent(baseActivity.graphComponent)
            .viewModelModule(ViewModelModule())
            .build()
    }
}