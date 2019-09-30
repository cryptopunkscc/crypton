package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.module.ViewModelScope
import cc.cryptopunks.crypton.util.ViewModel
import cc.cryptopunks.crypton.util.text
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@ViewModelScope
class SignInViewModel @Inject constructor(
    private val accountViewModel: AccountViewModel,
    private val addAccount: AddAccountInteractor
) : ViewModel {

    init {
        accountViewModel.apply {
            serviceName.text = "test.io"
            userName.text = "test"
            password.text = "test"
        }
    }

    suspend operator fun invoke() = coroutineScope {
        launch { accountViewModel() }
        launch {
            accountViewModel.onClick
                .asFlow()
                .filter { it > 0 }
                .map { accountViewModel.account }
                .collect { addAccount(it) }
        }
    }
}