package cc.cryptopunks.crypton.app.util

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.app.App
import cc.cryptopunks.crypton.app.DaggerContextComponent
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.module.ContextModule
import cc.cryptopunks.crypton.app.module.GraphModule
import cc.cryptopunks.crypton.app.ui.component.ActivityComponent
import cc.cryptopunks.crypton.app.ui.component.DaggerActivityComponent
import cc.cryptopunks.crypton.app.ui.component.DaggerGraphComponent
import cc.cryptopunks.crypton.app.ui.component.GraphComponent
import cc.cryptopunks.crypton.common.OptionItemSelectedBroadcast
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity :
    AppCompatActivity(),
    DisposableDelegate {

    val toolbar: Toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

    override val disposable = CompositeDisposable()

    private val broadcastItemSelected = OptionItemSelectedBroadcast()

    private val app get() = application as App

    val applicationComponent get() = app.component

    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent
            .builder()
            .contextComponent(
                DaggerContextComponent
                    .builder()
                    .appComponent(applicationComponent)
                    .contextModule(ContextModule(this))
                    .build()
            )
            .build()
    }

    val graphComponent: GraphComponent by lazy {
        fragment("graph") {
            DependenciesFragment(
                DaggerGraphComponent.builder()
                    .appComponent(applicationComponent)
                    .graphModule(GraphModule())
                    .build()
            )
        }.component
    }

    open val navController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applicationComponent.activityStackCache { plus(this@BaseActivity) }
    }

    override fun onDestroy() {
        disposable.dispose()
        applicationComponent.activityStackCache { minus(this@BaseActivity) }
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        broadcastItemSelected(item)
        return super.onOptionsItemSelected(item)
    }
}