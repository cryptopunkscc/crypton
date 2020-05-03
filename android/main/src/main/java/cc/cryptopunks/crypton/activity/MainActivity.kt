package cc.cryptopunks.crypton.activity

import android.content.Intent
import android.os.Bundle
import cc.cryptopunks.crypton.detachDebugDrawer
import cc.cryptopunks.crypton.fragment.MainFragment
import cc.cryptopunks.crypton.initDebugDrawer
import cc.cryptopunks.crypton.intent.IntentProcessor
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.service.top
import cc.cryptopunks.crypton.util.ext.fragment
import cc.cryptopunks.crypton.view.RosterView

class MainActivity : FeatureActivity() {

    private val processIntent by lazy {
        IntentProcessor(appCore.clipboardRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initDebugDrawer()
        fragment<MainFragment>()
        intent?.let(processIntent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let(processIntent)
    }

    override fun onDestroy() {
        detachDebugDrawer()
        super.onDestroy()
    }

    override fun onBackPressed() {
        // prevent activity from leaking due to android bug
        // https://issuetracker.google.com/issues/139738913
        appCore.connectableBindingsStore.top()?.run {
            if (services.any { it is RosterView })
                finishAfterTransition() else
                super.onBackPressed()
        }
    }
}
