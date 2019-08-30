package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.util.Navigate
import cc.cryptopunks.crypton.util.OptionItemSelected
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    optionItemSelected: OptionItemSelected,
    navigate: Navigate
) : () -> Disposable by {
    optionItemSelected
        .observable()
        .subscribe(navigate)
}