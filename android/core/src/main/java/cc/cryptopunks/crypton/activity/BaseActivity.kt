package cc.cryptopunks.crypton.activity

import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.AppCore
import cc.cryptopunks.crypton.core.R

abstract class BaseActivity : AppCompatActivity()  {

    val core get() = (application as AppCore).component

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val navController by lazy { findNavController(R.id.navHost) }
}