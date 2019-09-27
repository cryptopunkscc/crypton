package cc.cryptopunks.crypton.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import cc.cryptopunks.crypton.R
import cc.cryptopunks.crypton.presentation.viewmodel.DashboardViewModel
import cc.cryptopunks.crypton.presentation.viewmodel.OptionItemNavigationViewModel
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardFragment : BaseAppFragment() {

    override val layoutRes: Int get() = R.layout.dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelComponent.inject(this)
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
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}