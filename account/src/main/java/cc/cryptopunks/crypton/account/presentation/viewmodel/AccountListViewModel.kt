package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.domain.query.AccountList
import cc.cryptopunks.crypton.common.Schedulers
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.OptionItemSelected
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class AccountListViewModel @Inject constructor(
    private val accounts: AccountList,
    private val schedulers: Schedulers,
    private val navigation: Navigation,
    private val optionItemSelected: OptionItemSelected
) : () -> Disposable by {
    optionItemSelected.observable().subscribe { id ->
        navigation.navigate(id)
    }
} {
    val observable
        get() = accounts()
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)!!
}