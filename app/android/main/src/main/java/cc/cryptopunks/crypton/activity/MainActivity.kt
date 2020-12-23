package cc.cryptopunks.crypton.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cc.cryptopunks.crypton.context.clipboardRepo
import cc.cryptopunks.crypton.debug.drawer.detachDebugDrawer
import cc.cryptopunks.crypton.debug.drawer.initDebugDrawer
import cc.cryptopunks.crypton.intent.NewIntentProcessor
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.util.Buffer
import cc.cryptopunks.crypton.util.Log
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.view.setupDrawerAccountView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch

private val topLevelDestinations = setOf(
    R.id.splashFragment,
    R.id.rosterFragment
)

class MainActivity : FeatureActivity() {

    private val processIntent by lazy {
        NewIntentProcessor(rootScope.clipboardRepo)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        setSupportActionBar(toolbar)
        initDebugDrawer()
        intent?.let(processIntent)
        appBarConfiguration = AppBarConfiguration(topLevelDestinations, drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view).apply {
            setupWithNavController(navController)
            drawerToggleDelegate
            getHeaderView(0).setupDrawerAccountView(navController, appBarConfiguration)
        }
        launch { subscribeErrorDialog() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.let(processIntent)
    }

    override fun onDestroy() {
        detachDebugDrawer()
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

private suspend fun Activity.subscribeErrorDialog() =
    CoroutineLog.flow()
//        .scan(Buffer<Log.Event>()) { accumulator, value -> accumulator + value }
//        .mapNotNull { it.lastOrNull()?.throwable }
        .mapNotNull { it.throwable }
        .collect { throwable -> showErrorDialog(throwable) }

private fun Activity.showErrorDialog(throwable: Throwable) = AlertDialog
    .Builder(this)
    .setTitle(throwable.localizedMessage)
    .setMessage(throwable.stackTraceToString())
    .show()
