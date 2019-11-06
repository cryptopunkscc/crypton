package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.account.R
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.fragment.showRemoveAccountFragment
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.crypton.viewmodel.AccountItemViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.account_item.*
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Provider
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates.observable

class AccountListAdapter @Inject constructor(
    private val accountItemViewModelProvider: Provider<AccountItemViewModel>,
    private val fragmentManager: FragmentManager,
    override val coroutineContext: CoroutineContext
) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>(),
    CoroutineScope {

    var items by observable(listOf<Account>()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        containerView = parent.inflate(R.layout.account_item),
        model = accountItemViewModelProvider.get()
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        override val containerView: View,
        private val model: AccountItemViewModel
    ) : RecyclerView.ViewHolder(containerView),
        LayoutContainer,
        CoroutineScope by plus(Job()) {

        init {
            removeButton.setOnClickListener {
                fragmentManager.showRemoveAccountFragment(model)
            }
        }

        fun bind(account: Account) {
            coroutineContext.cancelChildren()

            model.account = account

            accountName.text = model.name
            status.text = model.status

            connectionSwitch.apply {
                setOnCheckedChangeListener(null)

                if (isChecked != model.isChecked)
                    isChecked = model.isChecked

                setOnCheckedChangeListener { _, _ ->
                    launch {
                        delay(400)
                        model.toggleConnection().join()
                    }
                }
            }
        }
    }
}