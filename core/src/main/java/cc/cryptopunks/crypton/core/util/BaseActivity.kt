package cc.cryptopunks.crypton.core.util

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cc.cryptopunks.crypton.common.OptionItemSelectedBroadcast
import cc.cryptopunks.crypton.core.App
import cc.cryptopunks.crypton.core.R
import cc.cryptopunks.crypton.core.component.*
import cc.cryptopunks.crypton.core.module.ContextModule
import cc.cryptopunks.crypton.core.module.GraphModule
import cc.cryptopunks.crypton.core.util.ext.fragment
import cc.cryptopunks.crypton.core.util.ext.invoke
import io.reactivex.disposables.CompositeDisposable


abstract class BaseActivity :
    AppCompatActivity(),
    DisposableDelegate {

    val toolbar by lazy { findViewById<Toolbar>(R.id.action_bar) }

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
                    .applicationComponent(applicationComponent)
                    .contextModule(ContextModule(this))
                    .build()
            )
            .build()
    }

    val graphComponent: GraphComponent by lazy {
        fragment("graph") {
            DependenciesFragment(
                DaggerGraphComponent.builder()
                    .applicationComponent(applicationComponent)
                    .graphModule(GraphModule())
                    .build()
            )
        }.component
    }

    open val navController by lazy {
        findNavController(R.id.navHost)
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