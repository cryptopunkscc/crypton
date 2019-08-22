package cc.cryptopunks.crypton.account.presentation.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.presentation.adapter.AccountListAdapter
import cc.cryptopunks.crypton.account.presentation.adapter.subscribe
import cc.cryptopunks.crypton.account.presentation.viewmodel.AccountItemViewModel
import cc.cryptopunks.crypton.account.presentation.viewmodel.AccountListViewModel
import kotlinx.android.synthetic.main.account_list.*
import javax.inject.Inject
import javax.inject.Provider

class AccountListFragment : BaseAccountFragment() {

    override val layoutId: Int get() = R.layout.account_list

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
        accountListViewModel: AccountListViewModel,
        accountItemViewModelProvider: Provider<AccountItemViewModel>
    ) {
        val accountListAdapter = AccountListAdapter(
            accountItemViewModelProvider = accountItemViewModelProvider,
            fragmentManager = fragmentManager!!
        )

        accountRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = accountListAdapter
        }

        viewDisposable.addAll(
            accountListAdapter,
            accountListViewModel.observable
                .subscribe(accountListAdapter)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_management, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.setAccount -> navController.navigate(R.id.action_accountListFragment_to_set_account_navigation)
        else -> null
    } != null
}