package cc.cryptopunks.crypton.activity

import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.component.NavigationComponent
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.fragment.ComponentHolderFragment
import cc.cryptopunks.crypton.module.NavigationModule
import cc.cryptopunks.crypton.util.Scope
import cc.cryptopunks.crypton.util.ext.bind
import cc.cryptopunks.crypton.util.ext.fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class CoreActivity :
    AppCompatActivity(),
    CoroutineScope {

    val navController by lazy { findNavController(R.id.navHost) }

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val navigationComponent by lazy {
        fragment("navigation") {
            ComponentHolderFragment<NavigationComponent>()
        }.initComponent {
            NavigationModule()
        }
    }

    val scope = Scope.View()

    override val coroutineContext: CoroutineContext get() = scope.coroutineContext

    override fun onStart() {
        super.onStart()
        launch { navigationComponent.navigateOutput.bind(navController) }
    }

    override fun onStop() {
        coroutineContext.cancelChildren()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        launch { navigationComponent.onOptionItemSelected(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}