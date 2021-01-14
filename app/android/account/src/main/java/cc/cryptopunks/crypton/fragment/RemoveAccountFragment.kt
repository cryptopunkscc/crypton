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
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.inScope
import cc.cryptopunks.crypton.service.start
import kotlinx.android.synthetic.main.delete_account_checkbox.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RemoveAccountFragment :
    DialogFragment(),
    OnClickListener,
    CoroutineScope {

    private lateinit var account: Address

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    private val isDeleteFromServerChecked get() = deleteOnServerCheckbox?.isChecked ?: false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
        .Builder(activity)
        .setMessage(R.string.remove_account_confirmation_message)
//        .apply { if (account.isConnected) setView(R.layout.delete_account_checkbox) } TODO
        .setPositiveButton(R.string.ok, this)
        .setNegativeButton(R.string.cancel, this)
        .create()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> launch { Exec.RemoveAccount().inScope(account).start {  } }
            BUTTON_NEGATIVE -> dismiss()
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    companion object : (Address) -> RemoveAccountFragment by {
        RemoveAccountFragment().apply { account = it }
    } {
        val TAG: String = RemoveAccountFragment::class.java.name
    }
}

fun FragmentManager.showRemoveAccountFragment(model: Address) =
    RemoveAccountFragment(model).show(this, RemoveAccountFragment.TAG)
