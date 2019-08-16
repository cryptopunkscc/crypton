package cc.cryptopunks.crypton.app.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.databinding.MainBinding
import cc.cryptopunks.crypton.app.service.StartXmppService
import cc.cryptopunks.crypton.app.util.DataBindingActivity

class MainActivity : DataBindingActivity<MainBinding>() {

    override val layoutId: Int get() = R.layout.main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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