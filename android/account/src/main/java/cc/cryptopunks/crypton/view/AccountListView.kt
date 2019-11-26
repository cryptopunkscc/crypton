package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.adapter.AccountListAdapter
import cc.cryptopunks.crypton.entity.Address
import cc.cryptopunks.crypton.presenter.AccountListPresenter
import cc.cryptopunks.crypton.viewmodel.AccountItemViewModel
import kotlinx.android.synthetic.main.account_list.view.*
import javax.inject.Provider

class AccountListView(
    context: Context,
    scope: Actor.Scope,
    accountItemViewModelProvider: Provider<AccountItemViewModel>,
    fragmentManager: FragmentManager
) :
    FrameLayout(context),
    AccountListPresenter.Actor {

    private val accountListAdapter = AccountListAdapter(
        accountItemViewModelProvider = accountItemViewModelProvider,
        fragmentManager = fragmentManager,
        coroutineContext = scope.coroutineContext
    )

    init {
        View.inflate(
            context,
            R.layout.account_list,
            this
        )
        accountRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = accountListAdapter
        }
    }

    override val setAccounts: suspend (List<Address>) -> Unit
        get() = { accounts -> accountListAdapter.items = accounts }

}