package cc.cryptopunks.crypton.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.app.R
import cc.cryptopunks.crypton.core.entity.Account
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.account_item.*
import kotlin.properties.Delegates.observable

class AccountListAdapter : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    var items by observable(listOf<Account>()) { _, _, _ -> notifyDataSetChanged() }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.account_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        override val containerView: View
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(account: Account) {
            accountName.text = account.jid
            status.text = account.status.name
        }
    }
}

fun Observable<List<Account>>.subscribe(adapter: AccountListAdapter): Disposable = subscribe {
    adapter.items = it
}