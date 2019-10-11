package cc.cryptopunks.crypton.view

import android.view.View
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import cc.cryptopunks.crypton.actor.Actor
import cc.cryptopunks.crypton.adapter.MessageAdapter
import cc.cryptopunks.crypton.feature.chat.presenter.ChatPresenter
import cc.cryptopunks.crypton.feature.chat.presenter.MessagePresenter
import cc.cryptopunks.crypton.util.bindings.clicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.chat.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatView @Inject constructor(
    override val containerView: View,
    scope: Actor.Scope
) :
    LayoutContainer,
    ChatPresenter.View {

    private val messageAdapter = MessageAdapter(scope)

    init {
        chatRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override val setMessages: suspend (PagedList<MessagePresenter>) -> Unit get() = {
        messageAdapter.submitList(it)
    }

    override val sendMessageFlow: Flow<String> = sendMessageButton
        .clicks()
        .map { messageInput.text.toString() }
}