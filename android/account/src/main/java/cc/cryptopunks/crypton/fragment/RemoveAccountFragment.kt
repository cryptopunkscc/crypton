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
import cc.cryptopunks.crypton.activity.BaseActivity
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Actor
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.CompoundHandler
import cc.cryptopunks.crypton.context.collect
import cc.cryptopunks.crypton.service.accountHandlers
import kotlinx.android.synthetic.main.delete_account_checkbox.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class RemoveAccountFragment :
    DialogFragment(),
    OnClickListener,
    Actor {

    private lateinit var model: Address

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
        .Builder(activity)
        .setMessage(R.string.remove_account_confirmation_message)
//        .apply { if (model.isConnected) setView(R.layout.delete_account_checkbox) } TODO
        .setPositiveButton(R.string.ok, this)
        .setNegativeButton(R.string.cancel, this)
        .create()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> (context as BaseActivity).appScope.run {
                launch { flowOf(Account.Service.Remove(model)).collect(CompoundHandler(accountHandlers())) }
            }
            BUTTON_NEGATIVE -> dismiss()
        }
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }

    private val isDeleteFromServerChecked get() = deleteOnServerCheckbox?.isChecked ?: false
    companion object : (Address) -> RemoveAccountFragment by {
        RemoveAccountFragment().apply { model = it }
    } {
        val TAG: String = RemoveAccountFragment::class.java.name
    }
}

fun FragmentManager.showRemoveAccountFragment(model: Address) =
    RemoveAccountFragment(model).show(this, RemoveAccountFragment.TAG)
