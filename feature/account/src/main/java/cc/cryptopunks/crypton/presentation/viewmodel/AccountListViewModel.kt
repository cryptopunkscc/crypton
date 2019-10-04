package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.AccountListSelector
import cc.cryptopunks.crypton.dagger.ViewModelScope
import javax.inject.Inject

@ViewModelScope
class AccountListViewModel @Inject constructor(
    private val getAccounts: AccountListSelector
) {
    val accounts get() = getAccounts()
}