package cc.cryptopunks.crypton.presentation.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cc.cryptopunks.crypton.conversation.R
import cc.cryptopunks.crypton.entity.User
import cc.cryptopunks.crypton.util.ext.inflate
import cc.cryptopunks.kache.rxjava.observable
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.conversation_user_item.*
import org.reactivestreams.Publisher
import javax.inject.Inject

class ConversationUserListAdapter @Inject constructor() :
    RecyclerView.Adapter<ConversationUserListAdapter.ViewHolder>() {

    private var users = emptyList<User>()

    operator fun invoke(publisher: Publisher<List<User>>) = publisher.observable().subscribe {
        users = it
        notifyDataSetChanged()
    }!!

    override fun getItemCount(): Int =
        users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.conversation_user_item))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(users[position])

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(user: User) {
            userNameTextView.text = user.remoteId
        }
    }
}