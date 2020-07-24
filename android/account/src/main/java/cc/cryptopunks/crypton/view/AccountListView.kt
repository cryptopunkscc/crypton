package cc.cryptopunks.crypton.view

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.adapter.AccountListAdapter
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.context.Subscribe
import cc.cryptopunks.crypton.widget.ActorLayout
import kotlinx.android.synthetic.main.account_list.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class AccountListView(
    context: Context,
    fragmentManager: FragmentManager
) :
    ActorLayout(context) {

    override val coroutineContext = SupervisorJob() + Dispatchers.Main

    private val accountListAdapter = AccountListAdapter(
        fragmentManager = fragmentManager,
        coroutineContext = coroutineContext
    )

    init {
        View.inflate(context, R.layout.account_list, this)
        accountRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = accountListAdapter
        }
    }

    override fun Connector.connect(): Job = launch {
        connect(accountListAdapter)
        launch {
            Get.Accounts.out()
            Subscribe.Accounts(true).out()
        }
    }
}
