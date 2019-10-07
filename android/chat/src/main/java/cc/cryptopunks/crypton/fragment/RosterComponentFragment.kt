package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.DaggerRosterComponent
import cc.cryptopunks.crypton.component.RosterComponent
import cc.cryptopunks.crypton.module.fragmentComponent
import cc.cryptopunks.crypton.module.viewModelComponent

abstract class RosterComponentFragment : CoreFragment() {

    val component: RosterComponent by lazy {
        DaggerRosterComponent.builder()
            .viewModelComponent(viewModelComponent())
            .fragmentComponent(fragmentComponent())
            .build()
    }
}