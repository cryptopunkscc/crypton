package cc.cryptopunks.crypton.app.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.app.ui.adapter.AccountListAdapter
import cc.cryptopunks.crypton.app.ui.adapter.subscribe
import cc.cryptopunks.crypton.app.ui.viewmodel.AccountListViewModel
import cc.cryptopunks.crypton.app.util.BaseFragment
import kotlinx.android.synthetic.main.account_list.*
import javax.inject.Inject

class AccountListFragment : BaseFragment() {

    override val layoutId: Int get() = R.layout.account_list

    override val titleId: Int get() = R.string.manage_accounts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModelComponent.inject(this)
    }

    @Inject
    fun init(
        accountListViewModel: AccountListViewModel
    ) {
        val accountsAdapter = AccountListAdapter()

        accountRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = accountsAdapter
        }

        viewDisposable.addAll(
            accountListViewModel.accounts.subscribe(accountsAdapter)
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