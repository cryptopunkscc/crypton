package cc.cryptopunks.crypton.app.presentation.viewmodel

import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.OptionItemSelectedBroadcast
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    onOptionItemSelectedBroadcast: OptionItemSelectedBroadcast,
    navigation: Navigation
) : () -> Disposable by {
    onOptionItemSelectedBroadcast
        .observable()
        .subscribe { menuItem ->
            navigation.navigate(menuItem.itemId)
        }
}