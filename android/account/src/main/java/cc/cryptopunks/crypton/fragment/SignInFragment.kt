package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.util.bindings.bind
import kotlinx.android.synthetic.main.sign_in.*
import kotlinx.coroutines.launch

class SignInFragment : AccountComponentFragment() {

    override val layoutRes: Int get() = R.layout.sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.signInViewModel.run {
            launch { invoke() }
            accountViewModel.run {
                launch { serviceNameLayout.bind(serviceName) }
                launch { userNameLayout.bind(userName) }
                launch { passwordLayout.bind(password) }
                launch { loginButton.bind(onClick) }
                launch { errorOutput.bind(errorMessage) }
            }
        }
    }
}