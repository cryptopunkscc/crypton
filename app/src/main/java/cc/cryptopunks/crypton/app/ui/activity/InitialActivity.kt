package cc.cryptopunks.crypton.app.ui.activity

import android.os.Bundle
import cc.cryptopunks.kache.rxjava.observable
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.ui.fragment.InitialNavigationFragment
import cc.cryptopunks.crypton.app.util.DataBindingActivity
import cc.cryptopunks.crypton.app.util.Navigation
import cc.cryptopunks.crypton.app.util.fragment
import cc.cryptopunks.crypton.app.util.subscribe

class InitialActivity :
    DataBindingActivity<cc.cryptopunks.crypton.app.databinding.InitialLayoutBinding>() {

    override val layoutId: Int get() = R.layout.initial_layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0);
        fragment<InitialNavigationFragment>()
        graphComponent.navigationBus.init()
    }

    fun Navigation.Bus.init() {
        disposable.addAll(
            observable()
                .doAfterNext {
                    if (it.actionId == R.id.navigate_to_dashboardActivity)
                        finish()
                }
                .subscribe(navController)
        )
    }
}