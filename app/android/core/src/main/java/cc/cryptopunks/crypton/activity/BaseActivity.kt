package cc.cryptopunks.crypton.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.core.R

abstract class BaseActivity : AppCompatActivity() {

    val rootScope: RootScope get() = application as RootScope

    val toolbar get() = findViewById<Toolbar>(R.id.action_bar)!!

    val navController get() = findNavController(R.id.navHost)
}
