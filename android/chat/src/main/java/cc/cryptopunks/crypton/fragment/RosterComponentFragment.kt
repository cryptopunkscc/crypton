package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.DaggerRosterComponent
import cc.cryptopunks.crypton.component.RosterComponent

abstract class RosterComponentFragment : CoreFragment() {

    val rosterComponent: RosterComponent by lazy {
        DaggerRosterComponent.builder()
            .presentationComponent(presentationComponent)
            .build()
    }
}