package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.selector.AccountListSelector
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
    private val getAccounts: AccountListSelector
) {
    val accounts get() = getAccounts()
}