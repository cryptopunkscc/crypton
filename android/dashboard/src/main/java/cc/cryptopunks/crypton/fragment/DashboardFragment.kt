package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.context.OptionItem
import cc.cryptopunks.crypton.presenter.DashboardPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import cc.cryptopunks.crypton.util.ext.resolve
import kotlinx.coroutines.flow.Flow

class DashboardFragment :
    DashboardPresenter.Actor,
    PresenterFragment<DashboardPresenter.Actor, DashboardPresenter>() {

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreatePresenter(): DashboardPresenter = featureCore
        .resolve<DashboardPresenter.Core>()
        .dashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.app_name)
    }

    override fun onCreateActor(view: View) = this

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override val accountManagementClick
        get() = (featureCore as OptionItem.Api).optionItemSelections

    override val createChatClick: Flow<Any>
        get() = view!!.findViewById<Button>(R.id.createConversationButton).clicks()
}