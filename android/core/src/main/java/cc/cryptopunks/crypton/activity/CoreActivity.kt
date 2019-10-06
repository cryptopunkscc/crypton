package cc.cryptopunks.crypton.activity

import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.component.featureComponent
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.util.ext.bind
import kotlinx.coroutines.*

abstract class CoreActivity :
    AppCompatActivity(),
    CoroutineScope by MainScope() {

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val featureComponent by lazy { featureComponent() }

    open val navController by lazy { findNavController(R.id.navHost) }

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