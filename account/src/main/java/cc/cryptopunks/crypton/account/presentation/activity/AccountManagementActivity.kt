package cc.cryptopunks.crypton.account.presentation.activity

import android.os.Bundle
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.presentation.fragment.NavigationFragment
import cc.cryptopunks.crypton.core.util.BaseActivity
import cc.cryptopunks.crypton.core.util.ext.fragment

class AccountManagementActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_management)
        fragment<NavigationFragment>()
    }
}