package cc.cryptopunks.crypton.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Engine
import cc.cryptopunks.crypton.core.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseActivity : AppCompatActivity()  {

    val appScope: AppScope get() = (application as Engine).scope

    val toolbar by lazy { findViewById<MaterialToolbar>(R.id.action_bar)!! }

    val navController by lazy { findNavController(R.id.navHost) }
}
