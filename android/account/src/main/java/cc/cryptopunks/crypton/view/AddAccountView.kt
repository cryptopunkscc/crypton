package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.account.R
import kotlinx.android.synthetic.main.add_account.view.*

internal class AddAccountView(
    context: Context
) :
    FrameLayout(context) {

    init {
        View.inflate(context, R.layout.add_account, this)
        addButton.setOnClickListener { findNavController().navigate(R.id.navigateLogin) }
        registerButton.setOnClickListener { findNavController().navigate(R.id.navigateRegister) }
    }
}
