package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.SignInLayoutBinding
import cc.cryptopunks.crypton.app.ui.viewmodel.SignInViewModel
import cc.cryptopunks.crypton.app.util.DataBindingFragment
import cc.cryptopunks.crypton.app.util.bind
import javax.inject.Inject

class SignInFragment : DataBindingFragment<SignInLayoutBinding>() {

    override val layoutId: Int get() = R.layout.sign_in_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        signInViewModel: SignInViewModel
    ) {
        binding?.apply {
            root.bind(viewDisposables)
            model = signInViewModel.accountViewModel
        }
        viewDisposables.addAll(signInViewModel())
    }
}