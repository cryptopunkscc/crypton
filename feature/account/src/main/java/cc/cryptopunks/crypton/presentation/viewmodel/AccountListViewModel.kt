package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.AccountListSelector
import javax.inject.Inject

class AccountListViewModel @Inject constructor(
    private val getAccounts: AccountListSelector
) {
    val accounts get() = getAccounts()
}