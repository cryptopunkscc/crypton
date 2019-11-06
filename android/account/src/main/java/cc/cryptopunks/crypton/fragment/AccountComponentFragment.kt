package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.component.AccountPresentationComponent

abstract class AccountComponentFragment : FeatureFragment() {
    val component get() = feature as AccountPresentationComponent
}