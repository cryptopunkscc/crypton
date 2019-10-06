package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import cc.cryptopunks.crypton.component.DaggerDashboardComponent
import cc.cryptopunks.crypton.component.DashboardComponent
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.feature.dashboard.viewmodel.DashboardViewModel
import cc.cryptopunks.crypton.module.viewModelComponent
import cc.cryptopunks.crypton.util.model.OptionItemNavigationModel
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class DashboardFragment : CoreFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    private val component: DashboardComponent by lazy {
        DaggerDashboardComponent.builder()
            .viewModelComponent(viewModelComponent())
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        component.inject(this)
    }

    @Inject
    fun init(
        dashboardViewModel: DashboardViewModel,
        optionItemNavigationModel: OptionItemNavigationModel
    ) {
        launch { optionItemNavigationModel() }
        createConversationButton.setOnClickListener {
            dashboardViewModel.createConversation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}