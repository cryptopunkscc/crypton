package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.navigation.Navigation
import cc.cryptopunks.crypton.presenter.DashboardPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.coroutines.flow.Flow


class DashboardFragment :
    DashboardPresenter.View,
    PresenterFragment<DashboardPresenter.View, DashboardPresenter>() {

    @dagger.Component(dependencies = [Navigation::class])
    interface Component : Presenter.Component<DashboardPresenter>

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreatePresenter(): DashboardPresenter = DaggerDashboardFragment_Component
        .builder()
        .navigation(navigation)
        .build()
        .presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateActor(view: View) = this

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override val accountManagementClick get() = coreActivity.navigationComponent.optionItemSelections

    override val createChatClick: Flow<Any>
        get() = view!!.findViewById<Button>(R.id.createConversationButton).clicks()
}