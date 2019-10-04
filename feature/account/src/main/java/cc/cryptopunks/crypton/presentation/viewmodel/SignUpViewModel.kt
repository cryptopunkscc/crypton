package cc.cryptopunks.crypton.presentation.viewmodel

import cc.cryptopunks.crypton.domain.interactor.RegisterAccountInteractor
import cc.cryptopunks.crypton.dagger.ViewModelScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import javax.inject.Inject

@ViewModelScope
class SignUpViewModel @Inject constructor(
    private val accountViewModel: AccountViewModel,
    private val registerAccount: RegisterAccountInteractor
) {
    suspend operator fun invoke() = coroutineScope {
        launch { accountViewModel() }
        launch {
            accountViewModel
                .onClick
                .asFlow()
                .filter { it > 0 }
                .map { accountViewModel.account }
                .collect { registerAccount(it) }
        }
    }
}