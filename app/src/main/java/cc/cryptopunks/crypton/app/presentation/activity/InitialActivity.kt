package cc.cryptopunks.crypton.app.presentation.activity

import android.os.Bundle
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.presentation.fragment.NavigationFragment
import cc.cryptopunks.crypton.core.util.BaseActivity
import cc.cryptopunks.crypton.core.util.Navigation
import cc.cryptopunks.crypton.core.util.ext.fragment
import cc.cryptopunks.crypton.core.util.subscribe
import cc.cryptopunks.kache.rxjava.observable

class InitialActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initial)
        overridePendingTransition(0, 0)
        fragment<NavigationFragment>()
        graphComponent.navigationBus.init()
    }

    private fun Navigation.Bus.init() = disposable.addAll(
        observable().doAfterNext {
            if (it.actionId == R.id.navigate_to_dashboardActivity)
                finish()
        }.subscribe(navController)
    )
}