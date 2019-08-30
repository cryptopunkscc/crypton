package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.presentation.viewmodel.AccountViewModel
import cc.cryptopunks.crypton.presentation.viewmodel.SignUpViewModel
import cc.cryptopunks.crypton.util.bind
import kotlinx.android.synthetic.main.sign_up.*
import javax.inject.Inject

class SignUpFragment : BaseAccountFragment() {

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
        with(accountViewModel) {
            viewDisposable.addAll(
                serviceNameLayout.bind(serviceName),
                userNameLayout.bind(userName),
                passwordLayout.bind(password),
                confirmPasswordLayout.bind(confirmPassword),
                registerButton.bind(onClick),
                errorOutput.bind(errorMessage)
            )
        }
        viewDisposable.addAll(
            signUpViewModel()
        )
    }
}