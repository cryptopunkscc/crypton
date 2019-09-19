package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.presentation.viewmodel.AccountViewModel
import cc.cryptopunks.crypton.presentation.viewmodel.SignUpViewModel
import cc.cryptopunks.crypton.util.reactivebindings.bind
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignUpFragment : AccountComponentFragment() {

    override val layoutId: Int get() = R.layout.sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        accountViewModel: AccountViewModel,
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