package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.selector.AccountListSelector
import cc.cryptopunks.crypton.util.Schedulers
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class AccountListViewModel @Inject constructor(
    private val accounts: AccountListSelector,
    private val schedulers: Schedulers,
    private val navigate: Navigate,
    private val optionItemSelected: OptionItemSelected
) : () -> Disposable by {
    optionItemSelected.observable().subscribe(navigate)
} {
    val observable
        get() = accounts()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)!!
}