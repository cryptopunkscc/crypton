package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import cc.cryptopunks.crypton.component.PresentationComponent
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.feature.dashboard.presenter.DashboardPresenter
import cc.cryptopunks.crypton.presenter.Presenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.coroutines.flow.Flow


class DashboardFragment :
    DashboardPresenter.View,
    PresenterFragment<
            DashboardPresenter.View,
            DashboardPresenter,
            DashboardFragment.Component>() {

    override val layoutRes: Int get() = R.layout.dashboard

    @dagger.Component(dependencies = [PresentationComponent::class])
    interface Component : Presenter.Component<DashboardPresenter>


    override suspend fun onCreateComponent(
        component: PresentationComponent
    ): Component = DaggerDashboardFragment_Component
        .builder()
        .presentationComponent(component)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateActor(view: View): DashboardPresenter.View = this

    override val accountManagementClick get() = coreActivity.navigationComponent.optionItemSelections

    override val createChatClick: Flow<Any>
        get() = view!!.findViewById<Button>(R.id.createConversationButton).clicks()
}