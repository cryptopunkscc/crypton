package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.adapter.AccountListAdapter
import cc.cryptopunks.crypton.adapter.bind
import cc.cryptopunks.crypton.feature.account.viewmodel.AccountItemViewModel
import cc.cryptopunks.crypton.feature.account.viewmodel.AccountListViewModel
import cc.cryptopunks.crypton.util.model.OptionItemNavigationModel
import kotlinx.android.synthetic.main.account_list.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

class AccountListFragment : AccountComponentFragment() {

    override val layoutRes: Int get() = R.layout.account_list

    override val titleId: Int get() = R.string.manage_accounts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        accountComponent.inject(this)
    }

    @Inject
    fun init(
        navigationModel: OptionItemNavigationModel,
        accountListViewModel: AccountListViewModel,
        accountItemViewModelProvider: Provider<AccountItemViewModel>
    ) {
        val accountListAdapter = AccountListAdapter(
            accountItemViewModelProvider = accountItemViewModelProvider,
            fragmentManager = fragmentManager!!,
            coroutineContext = coroutineContext
        )

        accountRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = accountListAdapter
        }

        launch { navigationModel() }
        launch { accountListAdapter.bind(accountListViewModel.accounts) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_management, menu)
    }
}