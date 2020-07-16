package cc.cryptopunks.crypton.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cc.cryptopunks.crypton.detachDebugDrawer
import cc.cryptopunks.crypton.initDebugDrawer
import cc.cryptopunks.crypton.intent.IntentProcessor
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.view.setupDrawerAccountView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main.*

private val topLevelDestinations = setOf(
    R.id.splashFragment,
    R.id.rosterFragment
)

class MainActivity : FeatureActivity() {

    private val processIntent by lazy {
        IntentProcessor(appScope.clipboardRepo)
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
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let(processIntent)
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
