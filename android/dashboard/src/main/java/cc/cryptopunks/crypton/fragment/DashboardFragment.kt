package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.module.CommonDomainModule
import cc.cryptopunks.crypton.view.DashboardView
import kotlinx.coroutines.runBlocking

class DashboardFragment :
    ServiceFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter() = CommonDomainModule(
        appCore = appCore
    ).routerService

    override fun onCreateActor(view: View) = DashboardView(
        containerView = view
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        runBlocking { binding.output(Route.AccountManagement) }
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
