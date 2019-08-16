package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.SignInBinding
import cc.cryptopunks.crypton.app.ui.viewmodel.SignInViewModel
import cc.cryptopunks.crypton.app.util.DataBindingFragment
import cc.cryptopunks.crypton.app.util.bind
import javax.inject.Inject

class SignInFragment : DataBindingFragment<SignInBinding>() {

    override val layoutId: Int get() = R.layout.sign_in

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        signInViewModel: SignInViewModel
    ) {
        binding?.apply {
            root.bind(viewDisposable)
            model = signInViewModel.accountViewModel
        }
        viewDisposable.addAll(signInViewModel())
    }
}