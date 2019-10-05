package cc.cryptopunks.crypton.util.ext

import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.feature.Route
import cc.cryptopunks.crypton.feature.Route.*

val Route.resId: Int
    get() = when (this) {
        is Raw -> id
        is Dashboard -> R.id.navigateDashboard
        is Roster -> R.id.navigateRoster
        is SetAccount -> R.id.navigateSetAccount
        is Login -> R.id.navigateLogin
        is Register -> R.id.navigateRegister
        is AccountList -> R.id.navigateAccountList
        is AccountManagement -> R.id.navigateAccountManagement
        is CreateChat -> R.id.navigateCreateChat
        is Chat -> R.id.navigateChat
        else -> throw Exception("Undefined resId for $this")
    }