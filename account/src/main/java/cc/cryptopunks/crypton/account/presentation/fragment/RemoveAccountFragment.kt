package cc.cryptopunks.crypton.account.presentation.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.*
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.presentation.viewmodel.AccountItemViewModel
import kotlinx.android.synthetic.main.delete_account_checkbox.*

class RemoveAccountFragment :
    DialogFragment(),
    OnClickListener {

    lateinit var model: AccountItemViewModel private set

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = AlertDialog
        .Builder(activity)
        .setMessage(getString(R.string.remove_account_confirmation_message))
        .apply { if (model.isChecked) setView(R.layout.delete_account_checkbox) }
        .setPositiveButton(R.string.ok, this)
        .setNegativeButton(R.string.cancel, this)
        .create()

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            BUTTON_POSITIVE -> model.remove(deleteFromServer)
            BUTTON_NEGATIVE -> dismiss()
        }
    }

    private val deleteFromServer get() = dialog.deleteOnServerCheckbox?.isChecked ?: false

    companion object : (AccountItemViewModel) -> RemoveAccountFragment by {
        RemoveAccountFragment().apply { model = it }
    } {
        val TAG: String = RemoveAccountFragment::class.java.name
    }
}

fun FragmentManager.showRemoveAccountFragment(model: AccountItemViewModel) =
    RemoveAccountFragment(model).show(this, RemoveAccountFragment.TAG)