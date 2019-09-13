package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.CreateAccountInteractor
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.ViewModel
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class SignUpViewModel @Inject constructor(
    val accountViewModel: AccountViewModel,
    createAccount: CreateAccountInteractor
) : ViewModel, () -> Disposable by {
    CompositeDisposable(
        accountViewModel(),
        accountViewModel
            .onClick
            .observable()
            .filter { it > 0 }
            .map { accountViewModel.getAccount() }
            .map(createAccount)
            .subscribe()
    )
}