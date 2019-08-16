package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.SignUpBinding
import cc.cryptopunks.crypton.app.ui.viewmodel.SignUpViewModel
import cc.cryptopunks.crypton.app.util.DataBindingFragment
import cc.cryptopunks.crypton.app.util.bind
import javax.inject.Inject

class SignUpFragment : DataBindingFragment<SignUpBinding>() {

    override val layoutId: Int get() = R.layout.sign_up

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        signUpViewModel: SignUpViewModel
    ) {
        binding?.apply {
            root.bind(viewDisposable)
            model = signUpViewModel.accountViewModel
        }
        viewDisposable.add(signUpViewModel())
    }
}