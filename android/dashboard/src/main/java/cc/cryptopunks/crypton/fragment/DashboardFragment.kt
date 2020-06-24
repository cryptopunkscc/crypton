package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.service.RouterService
import cc.cryptopunks.crypton.view.DashboardView

class DashboardFragment :
    ServiceFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateService() = RouterService(appScope)

    override fun onCreateActor(view: View) = DashboardView(view)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.navigateAccountManagement)
        return true
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
