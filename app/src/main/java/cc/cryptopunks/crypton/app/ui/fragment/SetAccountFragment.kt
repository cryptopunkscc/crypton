package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.SetAccountBinding
import cc.cryptopunks.crypton.app.ui.viewmodel.SetAccountViewModel
import cc.cryptopunks.crypton.app.util.DataBindingFragment
import javax.inject.Inject

class SetAccountFragment : DataBindingFragment<SetAccountBinding>() {

    override val layoutId: Int get() = R.layout.set_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        setAccountViewModel: SetAccountViewModel
    ) {
        binding?.apply {
            model = setAccountViewModel
        }
    }
}