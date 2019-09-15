package cc.cryptopunks.crypton.presentation.fragment

import cc.cryptopunks.crypton.component.AccountComponent
import cc.cryptopunks.crypton.component.DaggerAccountComponent
import cc.cryptopunks.crypton.util.BaseFragment

abstract class AccountComponentFragment : BaseFragment() {

    val accountComponent: AccountComponent by lazy {
        DaggerAccountComponent
            .builder()
            .featureComponent(baseActivity.featureComponent)
            .build()
    }
}