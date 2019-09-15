package cc.cryptopunks.crypton.presentation.fragment


import android.os.Bundle
import android.view.View
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.presentation.viewmodel.SetAccountViewModel
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.set_account.*
import javax.inject.Inject


class SetAccountFragment : AccountComponentFragment() {

    override val layoutId: Int get() = R.layout.set_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        setAccountViewModel: SetAccountViewModel
    ) {
        with(setAccountViewModel) {
            viewDisposable.addAll(
                addButton.clicks().subscribe { addAccount() },
                registerButton.clicks().subscribe { registerAccount() }
            )
        }
    }
}