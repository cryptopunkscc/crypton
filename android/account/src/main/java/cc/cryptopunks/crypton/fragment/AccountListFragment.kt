package cc.cryptopunks.crypton.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.ViewGroup
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.service.AccountListService
import cc.cryptopunks.crypton.util.ext.resolve
import cc.cryptopunks.crypton.view.AccountListView


class AccountListFragment : ServiceFragment() {

    override val titleId: Int get() = R.string.manage_accounts

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreatePresenter() = featureCore
        .resolve<AccountListService.Core>()
        .accountListService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AccountListView(
        context = activity!!,
        fragmentManager = fragmentManager!!
    )

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_management, menu)
    }
}