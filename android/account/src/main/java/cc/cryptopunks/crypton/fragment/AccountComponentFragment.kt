package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.AccountComponent
import cc.cryptopunks.crypton.component.DaggerAccountComponent
import cc.cryptopunks.crypton.coreComponent

abstract class AccountComponentFragment : CoreFragment() {

    val accountComponent: AccountComponent by lazy {
        DaggerAccountComponent
            .builder()
            .component(coreComponent)
            .component(navigationComponent)
            .build()
    }
}