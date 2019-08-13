package cc.cryptopunks.crypton.app.ui.viewmodel

import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.util.Navigation
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    navigation: Navigation.Bus
) : Navigation.Bus by navigation {

    fun dashboard(): Unit = navigate(R.id.navigate_to_dashboardActivity)

    fun addAccount(): Unit = navigate(R.id.navigate_setAccountFragment_to_signInFragment)

    fun registerAccount(): Unit = navigate(R.id.navigate_setAccountFragment_to_signUpFragment)
}