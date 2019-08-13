package cc.cryptopunks.crypton.app.ui.viewmodel

import cc.cryptopunks.crypton.core.interactor.AddAccount
import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.app.module.ViewModelScope
import cc.cryptopunks.crypton.app.util.AsyncExecutor
import cc.cryptopunks.crypton.app.util.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class SignInViewModel @Inject constructor(
    val accountViewModel: AccountViewModel,
    async: AsyncExecutor,
    addAccount: AddAccount
) : ViewModel, () -> Disposable by {
    CompositeDisposable(
        accountViewModel(),
        accountViewModel
            .onClick
            .observable()
            .filter { it > 0 }
            .map { accountViewModel.getAccount() }
            .subscribe(async(task = addAccount))
    )
}