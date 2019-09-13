package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.ViewModel
import cc.cryptopunks.crypton.util.text
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@ViewModelScope
class SignInViewModel @Inject constructor(
    accountViewModel: AccountViewModel,
    addAccount: AddAccountInteractor
) : ViewModel, () -> Disposable by {
    CompositeDisposable(
        accountViewModel(),
        accountViewModel
            .onClick
            .observable()
            .filter { it > 0 }
            .map { accountViewModel.getAccount() }
            .map(addAccount)
            .subscribe()
    )
} {
    init {
        accountViewModel.apply {
            serviceName.text = "test.io"
            userName.text = "test"
            password.text = "test"
        }
    }
}