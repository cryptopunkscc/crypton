package cc.cryptopunks.crypton.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.chat.R
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.util.ext.inflate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.chat_user_item.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ChatUserListAdapter @Inject constructor() :
    RecyclerView.Adapter<ChatUserListAdapter.ViewHolder>() {

    var users = emptyList<Address>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int =
        users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.chat_user_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position])

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(user: Address) {
            userNameTextView.text = user.toString()
        }
    }
}

suspend fun ChatUserListAdapter.bind(
    flow: Flow<List<Address>>
) = flow.collect {
    users = it
}
