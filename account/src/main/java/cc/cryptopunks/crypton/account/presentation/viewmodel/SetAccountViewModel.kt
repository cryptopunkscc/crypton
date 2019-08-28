package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.account.R
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    navigation: Navigation.Bus
) : Navigation.Bus by navigation {

    fun addAccount(): Unit = navigate(R.id.navigateSignIn)

    fun registerAccount(): Unit = navigate(R.id.navigateSignUp)
}