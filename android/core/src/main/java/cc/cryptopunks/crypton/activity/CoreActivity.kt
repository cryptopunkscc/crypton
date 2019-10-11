package cc.cryptopunks.crypton.activity

import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.fragment.ComponentHolderFragment
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.navigation.NavigationModule
import cc.cryptopunks.crypton.util.ext.bind
import cc.cryptopunks.crypton.util.ext.fragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

abstract class CoreActivity : AppCompatActivity() {

    val navController by lazy { findNavController(R.id.navHost) }

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val navigationComponent by lazy {
        fragment("navigation") {
            ComponentHolderFragment<Navigation.Component>()
        }.initComponent {
            NavigationModule()
        }
    }

    private val scope = MainScope()

    override fun onStart() {
        super.onStart()
        scope.launch { navigationComponent.navigateOutput.bind(navController) }
    }

    override fun onStop() {
        scope.coroutineContext.cancelChildren()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        scope.launch { navigationComponent.onOptionItemSelected(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}