package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.AccountComponent
import cc.cryptopunks.crypton.component.DaggerAccountComponent

abstract class AccountComponentFragment : CoreFragment() {

    val accountComponent: AccountComponent by lazy {
        DaggerAccountComponent
            .builder()
            .presentationComponent(presentationComponent)
            .build()
    }
}