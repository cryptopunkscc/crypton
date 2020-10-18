package cc.cryptopunks.crypton.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import cc.cryptopunks.crypton.Actor
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.inContext
import cc.cryptopunks.crypton.dispatch
import cc.cryptopunks.crypton.service
import cc.cryptopunks.crypton.serviceName
import kotlinx.android.synthetic.main.delete_account_checkbox.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.util.concurrent.CancellationException

class RemoveAccountFragment :
    DialogFragment(),
    OnClickListener,
    Actor {

    private lateinit var account: Address

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
        .Builder(activity)
        .setMessage(R.string.remove_account_confirmation_message)
//        .apply { if (account.isConnected) setView(R.layout.delete_account_checkbox) } TODO
        .setPositiveButton(R.string.ok, this)
        .setNegativeButton(R.string.cancel, this)
        .create()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> rootScope.service(serviceName).dispatch(Exec.RemoveAccount().inContext(account))
            BUTTON_NEGATIVE -> dismiss()
        }
    }

    override fun onDestroy() {
        cancel(CancellationException(toString()))
        super.onDestroy()
    }

    private val isDeleteFromServerChecked get() = deleteOnServerCheckbox?.isChecked ?: false
    companion object : (Address) -> RemoveAccountFragment by {
        RemoveAccountFragment().apply { account = it }
    } {
        val TAG: String = RemoveAccountFragment::class.java.name
    }
}

fun FragmentManager.showRemoveAccountFragment(model: Address) =
    RemoveAccountFragment(model).show(this, RemoveAccountFragment.TAG)
