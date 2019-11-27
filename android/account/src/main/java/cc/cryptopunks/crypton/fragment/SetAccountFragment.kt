package cc.cryptopunks.crypton.fragment


import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import kotlinx.android.synthetic.main.set_account.*


class SetAccountFragment : AccountCoreFragment() {

    override val layoutRes: Int get() = R.layout.set_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        core.setAccountViewModel.run {
            addButton.setOnClickListener { addAccount() }
            registerButton.setOnClickListener { registerAccount() }
        }
    }
}