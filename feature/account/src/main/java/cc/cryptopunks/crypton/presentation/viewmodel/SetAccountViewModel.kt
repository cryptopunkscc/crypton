package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.util.Navigate
import javax.inject.Inject

class SetAccountViewModel @Inject constructor(
    private val navigate: Navigate
) {

    fun addAccount(): Unit = navigate(R.id.navigateSignIn)

    fun registerAccount(): Unit = navigate(R.id.navigateSignUp)
}