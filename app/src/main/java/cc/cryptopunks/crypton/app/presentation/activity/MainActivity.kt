package cc.cryptopunks.crypton.app.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.service.StartXmppService
import cc.cryptopunks.crypton.account.presentation.activity.AccountManagementActivity
import cc.cryptopunks.crypton.core.util.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        StartXmppService(this).invoke()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.manageAccounts -> startActivity(Intent(this, AccountManagementActivity::class.java))
        else -> null
    } != null
}