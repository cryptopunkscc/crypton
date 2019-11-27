package cc.cryptopunks.crypton.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.Engine
import cc.cryptopunks.crypton.core.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity : AppCompatActivity()  {

    val appCore get() = (application as Engine).core

    val toolbar by lazy { findViewById<MaterialToolbar>(R.id.action_bar)!! }

    val navController by lazy { findNavController(R.id.navHost) }
}