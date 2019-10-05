package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import cc.cryptopunks.crypton.dagger.DaggerDashboardComponent
import cc.cryptopunks.crypton.dagger.DaggerFeatureModule
import cc.cryptopunks.crypton.dagger.DashboardComponent
import cc.cryptopunks.crypton.dashboard.R
import cc.cryptopunks.crypton.presentation.viewmodel.OptionItemNavigationViewModel
import cc.cryptopunks.crypton.util.BaseFragment
import cc.cryptopunks.crypton.presentation.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.launch
import javax.inject.Inject


class DashboardFragment : BaseFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    private val component: DashboardComponent by lazy {
        DaggerDashboardComponent.builder()
            .daggerFeatureModule(DaggerFeatureModule(baseActivity.featureComponent))
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
        optionItemNavigationViewModel: OptionItemNavigationViewModel
    ) {
        launch { optionItemNavigationViewModel() }
        createConversationButton.setOnClickListener {
            dashboardViewModel.createConversation()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}