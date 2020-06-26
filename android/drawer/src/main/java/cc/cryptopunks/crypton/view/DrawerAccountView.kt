package cc.cryptopunks.crypton.view

import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import cc.cryptopunks.crypton.context.address
import cc.cryptopunks.crypton.drawer.R
import cc.cryptopunks.crypton.navigate.currentAccount
import cc.cryptopunks.crypton.navigate.sharedPrefs
import cc.cryptopunks.crypton.util.letterColors
import kotlinx.android.synthetic.main.drawer_account_view.view.*

fun View.setupDrawerAccountView(
    navController: NavController,
    appBarConfiguration: AppBarConfiguration
) {
    val changeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        when(key) {
            "currentAccount" -> sharedPreferences.getString(key, "")?.let { address ->
                setAddress(address)
            }
        }
    }
    doOnAttach {
        setAddress(context.currentAccount.id)
        context.sharedPrefs.registerOnSharedPreferenceChangeListener(changeListener)
        setOnClickListener {
            navController.navigate(R.id.navigateAccountList)
            appBarConfiguration.openableLayout!!.close()
        }
    }
    doOnDetach {
        context.sharedPrefs.unregisterOnSharedPreferenceChangeListener(changeListener)
    }
}

private fun View.setAddress(address: String) {
    accountLetter.text = address.first().toString()
    accountName.text = address(address).local
    accountAddress.text = address
    address.first().let { letter ->
        ContextCompat.getColor(context, letterColors.getValue(letter))
    }.let { color ->
        (accountLetter.background as GradientDrawable).setColor(color)
    }
}
