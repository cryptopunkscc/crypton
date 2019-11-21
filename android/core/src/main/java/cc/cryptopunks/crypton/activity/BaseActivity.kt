package cc.cryptopunks.crypton.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.AppCore
import cc.cryptopunks.crypton.core.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity : AppCompatActivity()  {

    val core get() = (application as AppCore).component

    val toolbar by lazy { findViewById<MaterialToolbar>(R.id.action_bar)!! }

    val navController by lazy { findNavController(R.id.navHost) }
}