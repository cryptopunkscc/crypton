package cc.cryptopunks.crypton.activity

import android.os.Bundle
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.fragment.AccountNavigationFragment
import cc.cryptopunks.crypton.util.ext.fragment

class AccountManagementActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_management)
        fragment<AccountNavigationFragment>()
    }
}