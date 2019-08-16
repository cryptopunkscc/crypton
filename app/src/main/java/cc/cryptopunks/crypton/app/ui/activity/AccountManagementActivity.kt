package cc.cryptopunks.crypton.app.ui.activity

import android.os.Bundle
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.util.BaseActivity
import cc.cryptopunks.crypton.app.util.Navigation
import cc.cryptopunks.crypton.app.util.subscribe
import cc.cryptopunks.kache.rxjava.observable

class AccountManagementActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_management)
        graphComponent.navigationBus.init()
    }

    fun Navigation.Bus.init() = disposable.add(
        observable().subscribe(navController)
    )
}