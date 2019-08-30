package cc.cryptopunks.crypton.account.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.account.presentation.fragment.showRemoveAccountFragment
import cc.cryptopunks.crypton.account.presentation.viewmodel.AccountItemViewModel
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.core.util.DisposableDelegate
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.checkedChanges
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.account_item.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import kotlin.properties.Delegates.observable

class AccountListAdapter @Inject constructor(
    private val accountItemViewModelProvider: Provider<AccountItemViewModel>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>(),
    DisposableDelegate {

    override val disposable = CompositeDisposable()

    var items by observable(listOf<Account>()) { _, _, _ -> notifyDataSetChanged() }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        containerView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.account_item, parent, false),
        model = accountItemViewModelProvider.get(),
        showDialog = fragmentManager::showRemoveAccountFragment
    ).also { viewHolder ->
        disposable.add(viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        override val containerView: View,
        private val model: AccountItemViewModel,
        private val showDialog: (AccountItemViewModel) -> Unit
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer,
        DisposableDelegate {

        override val disposable = CompositeDisposable()

        fun bind(account: Account) {
            disposable.clear()
            model.account = account

            accountName.text = model.name
            status.text = model.status
            connectionSwitch.apply {
                if (isChecked != model.isChecked)
                    isChecked = model.isChecked
            }

            disposable.addAll(
                connectionSwitch.checkedChanges()
                    .skip(1)
                    .delay(400, TimeUnit.MILLISECONDS)
                    .subscribe {
                        model.toggleConnection()
                    },
                removeButton.clicks().subscribe {
                    showDialog(model)
                }
            )
        }
    }
}

fun Observable<List<Account>>.subscribe(adapter: AccountListAdapter): Disposable = subscribe {
    adapter.items = it
}