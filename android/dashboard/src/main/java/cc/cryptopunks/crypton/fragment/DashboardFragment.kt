package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.presenter.DashboardService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.DashboardView

class DashboardFragment :
    ServiceFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter() = featureCore
        .resolve<DashboardService.Core>()
        .dashboardService

    override fun onCreateActor(view: View) = DashboardView(
        containerView = view,
        optionItems = featureCore
            .resolve<OptionItem.Core>()
            .optionItemSelections
    )

    override fun onStart() {
        super.onStart()
        setTitle(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}