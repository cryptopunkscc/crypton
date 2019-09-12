package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.domain.selector.NewAccountConnected
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.Scopes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountNavigationViewModel @Inject constructor(
    scope: Scopes.ViewModel,
    private val newAccountConnected: NewAccountConnected,
    private val navigate: Navigate
) : CoroutineScope {

    override val coroutineContext = scope.launch {
        newAccountConnected().collect {
            navigate(R.id.navigateAccountList)
        }
    }
}