package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.core.util.AsyncExecutor
import cc.cryptopunks.crypton.core.util.ViewModel
import cc.cryptopunks.crypton.account.domain.command.AddAccount
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class SignInViewModel @Inject constructor(
    accountViewModel: AccountViewModel,
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