package cc.cryptopunks.crypton.app.ui.activity

import android.os.Bundle
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.InitialBinding
import cc.cryptopunks.crypton.app.ui.fragment.InitialNavigationFragment
import cc.cryptopunks.crypton.app.util.DataBindingActivity
import cc.cryptopunks.crypton.app.util.Navigation
import cc.cryptopunks.crypton.app.util.fragment
import cc.cryptopunks.crypton.app.util.subscribe
import cc.cryptopunks.kache.rxjava.observable

class InitialActivity : DataBindingActivity<InitialBinding>() {

    override val layoutId: Int get() = R.layout.initial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        fragment<InitialNavigationFragment>()
        graphComponent.navigationBus.init()
    }

    fun Navigation.Bus.init() = disposable.addAll(
        observable().doAfterNext {
            if (it.actionId == R.id.navigate_to_dashboardActivity)
                finish()
        }.subscribe(navController)
    )
}