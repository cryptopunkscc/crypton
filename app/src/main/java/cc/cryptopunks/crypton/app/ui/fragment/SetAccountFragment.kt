package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.SetAccountLayoutBinding
import cc.cryptopunks.crypton.app.ui.viewmodel.SetAccountViewModel
import cc.cryptopunks.crypton.app.util.DataBindingFragment
import javax.inject.Inject

class SetAccountFragment : DataBindingFragment<SetAccountLayoutBinding>() {

    override val layoutId: Int get() = R.layout.set_account_layout

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