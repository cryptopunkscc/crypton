package cc.cryptopunks.crypton.util

import android.view.MenuItem
import android.widget.Toolbar
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.app
import cc.cryptopunks.crypton.component.createActivityComponent
import cc.cryptopunks.crypton.component.featureComponent
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BaseActivity :
    DisposableActivity(),
    CoroutineScope by MainScope() {

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    val activityComponent by lazy { createActivityComponent(app.component) }

    val featureComponent by lazy { featureComponent(app.component) }

    open val navController by lazy { findNavController(R.id.navHost) }

    override fun CompositeDisposable.onStart() = addAll(
        featureComponent.navigationPublisher.observable().subscribe(navController)
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        launch { featureComponent.onOptionItemSelected(item.itemId) }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}