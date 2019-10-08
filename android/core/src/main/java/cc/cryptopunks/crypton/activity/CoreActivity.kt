package cc.cryptopunks.crypton.activity

import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.component.NavigationComponent
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.coreComponent
import cc.cryptopunks.crypton.fragment.ComponentHolderFragment
import cc.cryptopunks.crypton.module.NavigationModule
import cc.cryptopunks.crypton.util.ext.bind
import cc.cryptopunks.crypton.util.ext.fragment
import kotlinx.coroutines.*

abstract class CoreActivity :
    AppCompatActivity(),
    CoroutineScope by MainScope() {

    val navController by lazy { findNavController(R.id.navHost) }

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val featureComponent by lazy {
        fragment("feature") {
            ComponentHolderFragment<NavigationComponent>()
        }.initComponent {
            NavigationModule(coreComponent)
        }
    }

    override fun onStart() {
        super.onStart()
        launch { featureComponent.navigateOutput.bind(navController) }
    }

    override fun onStop() {
        coroutineContext.cancelChildren()
        super.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        launch { featureComponent.onOptionItemSelected(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}