package cc.cryptopunks.crypton.account.presentation.viewmodel

import cc.cryptopunks.crypton.account.domain.command.CreateAccount
import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.core.module.ViewModelScope
import cc.cryptopunks.crypton.core.util.AsyncExecutor
import cc.cryptopunks.crypton.core.util.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class SignUpViewModel @Inject constructor(
    val accountViewModel: AccountViewModel,
    async: AsyncExecutor,
    createAccount: CreateAccount
) : ViewModel, () -> Disposable by {
    CompositeDisposable(
        accountViewModel(),
        accountViewModel
            .onClick
            .observable()
            .filter { it > 0 }
            .map { accountViewModel.getAccount() }
            .subscribe(async(task = createAccount))
    )
}