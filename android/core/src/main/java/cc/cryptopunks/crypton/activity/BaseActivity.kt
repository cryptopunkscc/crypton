package cc.cryptopunks.crypton.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Engine
import cc.cryptopunks.crypton.core.R

abstract class BaseActivity : AppCompatActivity() {

    val appScope: AppScope get() = (application as Engine).scope

    val toolbar get() = findViewById<Toolbar>(R.id.action_bar)!!

    val navController get() = findNavController(R.id.navHost)
}
