package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.service.AccountService
import cc.cryptopunks.crypton.view.AccountListView


class AccountListFragment : ServiceFragment() {

    override val titleId: Int get() = R.string.manage_accounts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateService() =
        AccountService(appScope)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AccountListView(
        context = activity!!,
        fragmentManager = fragmentManager!!
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_management, menu)
    }
}
