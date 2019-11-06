package cc.cryptopunks.crypton.viewmodel

import cc.cryptopunks.crypton.interactor.AddAccountInteractor
import cc.cryptopunks.crypton.util.text
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInViewModel @Inject constructor(
    val accountViewModel: AccountFromViewModel,
    private val addAccount: AddAccountInteractor
) {

    init {
        accountViewModel.apply {
//            serviceName.text = "test.io"
//            userName.text = "test"
//            password.text = "test"
            serviceName.text = "cryptopunks.cc"
            userName.text = "test"
            password.text = "test"
        }
    }

    suspend operator fun invoke() = coroutineScope {
        launch { accountViewModel() }
        launch {
            accountViewModel.onClick
                .filter { it > 0 }
                .map { accountViewModel.account }
                .collect { addAccount(it) }
        }
    }
}