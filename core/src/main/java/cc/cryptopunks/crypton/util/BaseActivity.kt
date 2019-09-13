package cc.cryptopunks.crypton.util

import android.view.MenuItem
import android.widget.Toolbar
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.App
import cc.cryptopunks.crypton.component.*
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.module.ContextModule
import cc.cryptopunks.crypton.module.FeatureModule
import cc.cryptopunks.crypton.util.ext.fragment
import cc.cryptopunks.kache.rxjava.observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseActivity :
    DisposableActivity(),
    CoroutineScope by MainScope() {

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    private val app get() = application as App

    val applicationComponent get() = app.component

    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent
            .builder()
            .contextComponent(
                DaggerContextComponent
                    .builder()
                    .applicationComponent(applicationComponent)
                    .contextModule(ContextModule(this))
                    .build()
            )
            .build()
    }

    val featureComponent by lazy {
        fragment("feature") { DependenciesFragment<FeatureComponent>() }.also { fragment ->
            if (fragment.component == null)
            fragment.component = DaggerFeatureComponent.builder()
                .applicationComponent(applicationComponent)
                .featureModule(FeatureModule())
                .build()
        }.component!!
    }

    open val navController by lazy {
        findNavController(R.id.navHost)
    }

    override fun CompositeDisposable.onStart() = addAll (
        featureComponent.navigationPublisher.observable().subscribe(navController)
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        featureComponent.onOptionItemSelected(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}