package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.feature.account.viewmodel.AccountFromViewModel
import cc.cryptopunks.crypton.feature.account.viewmodel.SignUpViewModel
import cc.cryptopunks.crypton.util.reactivebindings.bind
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpFragment : AccountComponentFragment() {

    override val layoutRes: Int get() = R.layout.sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        accountViewModel: AccountFromViewModel,
        signUpViewModel: SignUpViewModel
    ) {
        launch { signUpViewModel() }
        with(accountViewModel) {
            launch { serviceNameLayout.bind(serviceName) }
            launch { userNameLayout.bind(userName) }
            launch { passwordLayout.bind(password) }
            launch { confirmPasswordLayout.bind(confirmPassword) }
            launch { registerButton.bind(onClick) }
            launch { errorOutput.bind(errorMessage) }
        }
    }
}