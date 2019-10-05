package cc.cryptopunks.crypton.feature.account.viewmodel

import cc.cryptopunks.crypton.feature.account.selector.AccountListSelector
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
    private val getAccounts: AccountListSelector
) {
    val accounts get() = getAccounts()
}