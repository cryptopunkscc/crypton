package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.fragment.showRemoveAccountFragment
import cc.cryptopunks.crypton.util.ext.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.account_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class AccountListAdapter(
    private val fragmentManager: FragmentManager,
    override val coroutineContext: CoroutineContext
) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>(),
    CoroutineScope,
    Connectable {

    private var items = listOf<Address>()

    private val channel = BroadcastChannel<Any>(1)

    override fun Connector.connect(): Job = launch {
        launch {
            input.filterIsInstance<Account.Service.Accounts>().collect {
                items = it.list
                notifyDataSetChanged()
            }
            channel.asFlow().collect(output)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        containerView = parent.inflate(R.layout.account_item)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        private var address = Address.Empty

        init {
            removeButton.setOnClickListener {
                fragmentManager.showRemoveAccountFragment(address)
            }
            connectionSwitch.setOnCheckedChangeListener { _, _ ->
                launch {
                    delay(400)
                    channel.send(Account.Service.Add())
                }
            }
        }

        fun bind(account: Address) {
            address = account
        }
    }
}
