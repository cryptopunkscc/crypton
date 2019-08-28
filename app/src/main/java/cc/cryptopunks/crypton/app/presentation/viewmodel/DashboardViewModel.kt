package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.OptionItemSelected
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    optionItemSelected: OptionItemSelected,
    navigation: Navigation
) : () -> Disposable by {
    optionItemSelected
        .observable()
        .subscribe { id ->
            navigation.navigate(id)
        }
}