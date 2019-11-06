package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.util.bindings.bind
import kotlinx.android.synthetic.main.sign_up.*
import kotlinx.coroutines.launch

class SignUpFragment : AccountComponentFragment() {

    override val layoutRes: Int get() = R.layout.sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.run {
            launch { signUpViewModel() }
            signUpViewModel.accountViewModel.run {
                launch { serviceNameLayout.bind(serviceName) }
                launch { userNameLayout.bind(userName) }
                launch { passwordLayout.bind(password) }
                launch { confirmPasswordLayout.bind(confirmPassword) }
                launch { registerButton.bind(onClick) }
                launch { errorOutput.bind(errorMessage) }
            }
        }
    }
}