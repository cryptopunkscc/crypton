package cc.cryptopunks.crypton.app.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.presentation.viewmodel.DashboardViewModel
import javax.inject.Inject

class DashboardFragment : BaseAppFragment() {

    override val layoutId: Int get() = R.layout.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(dashboardViewModel: DashboardViewModel) {
        modelDisposable.add(dashboardViewModel())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}