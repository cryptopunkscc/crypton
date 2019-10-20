package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.presentation.Presentation
import cc.cryptopunks.crypton.presenter.DaggerDashboardPresenter_Component
import cc.cryptopunks.crypton.presenter.DashboardPresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.coroutines.flow.Flow


class DashboardFragment :
    DashboardPresenter.View,
    CoreFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    private val manager = Presentation<DashboardPresenter.View, DashboardPresenter>()

    private val component: DashboardPresenter.Component by lazy {
        DaggerDashboardPresenter_Component
            .builder()
            .component(navigationComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        manager.setPresenter(component.presenter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager.setActor(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        manager.clearActor()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.apply {
            clearPresenter()
            cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override val accountManagementClick get() = coreActivity.navigationComponent.optionItemSelections

    override val createChatClick: Flow<Any>
        get() = view!!.findViewById<Button>(R.id.createConversationButton).clicks()
}