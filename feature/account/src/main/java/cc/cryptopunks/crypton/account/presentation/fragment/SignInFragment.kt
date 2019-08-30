package cc.cryptopunks.crypton.account.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.util.*
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.presentation.viewmodel.AccountViewModel
import cc.cryptopunks.crypton.account.presentation.viewmodel.SignInViewModel
import kotlinx.android.synthetic.main.sign_in.*
import javax.inject.Inject

class SignInFragment : BaseAccountFragment() {

    override val layoutId: Int get() = R.layout.sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        accountViewModel: AccountViewModel,
        signInViewModel: SignInViewModel
    ) {
        with(accountViewModel) {
            viewDisposable.addAll(
                serviceNameLayout.bind(serviceName),
                userNameLayout.bind(userName),
                passwordLayout.bind(password),
                loginButton.bind(onClick),
                errorOutput.bind(errorMessage)
            )
        }
        modelDisposable.addAll(
            signInViewModel()
        )
    }
}