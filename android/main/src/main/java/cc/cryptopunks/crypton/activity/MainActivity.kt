package cc.cryptopunks.crypton.activity

import android.os.Bundle
import cc.cryptopunks.crypton.detachDebugDrawer
import cc.cryptopunks.crypton.fragment.MainFragment
import cc.cryptopunks.crypton.initDebugDrawer
import cc.cryptopunks.crypton.main.R
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.util.ext.fragment
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.RosterView

class MainActivity : FeatureActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        initDebugDrawer()
        fragment<MainFragment>()
    }

    override fun onDestroy() {
        detachDebugDrawer()
        super.onDestroy()
    }

    override fun onBackPressed() {
        // prevent activity from leaking due to android bug
        // https://issuetracker.google.com/issues/139738913
        appCore.resolve<PresentationManager.Core>()
            .presentationManager.top().let {

            if (it == null || it.actor is RosterView)
                finishAfterTransition() else
                super.onBackPressed()
        }
    }
}