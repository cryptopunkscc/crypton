package cc.cryptopunks.crypton.fragment

import cc.cryptopunks.crypton.core.AccountPresentationCore

abstract class AccountCoreFragment : FeatureFragment() {
    val core get() = featureCore as AccountPresentationCore
}