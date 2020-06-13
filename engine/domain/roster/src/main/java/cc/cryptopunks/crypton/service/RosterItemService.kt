package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Connectable
import cc.cryptopunks.crypton.context.Connector
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.selector.LatestMessageFlowSelector
import cc.cryptopunks.crypton.selector.PresenceFlowSelector
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class RosterItemService private constructor(
    private val chat: Chat,
    private val navigate: Route.Navigate,
    private val presenceOf: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector,
    private val messageRepo: Message.Repo
) : Connectable {

    private data class UnreadMessages(val count: Int)

    object GetState

    override val id get() = chat.address.id

    private val store = Store(
        Roster.Item(
            letter = id.firstOrNull()?.toLowerCase() ?: 'a',
            title = id
        )
    )

    override val coroutineContext = store.dispatcher

    override fun Connector.connect() = launch {
        flowOf(
            input,
            presenceOf(chat.address),
            latestMessageFlow(chat.address),
            messageRepo.unreadCountFlow(chat.address).map {
                UnreadMessages(it)
            }
        )
            .flattenMerge()
            .mapNotNull { action -> reduce(action) }
            .collect(output)
    }

    private suspend fun reduce(action: Any): Roster.Item? = store reduce {
        when (action) {
            is Presence.Status -> {
                copy(presence = action)
            }
            is Message -> {
                copy(message = action)
            }
            is UnreadMessages -> {
                copy(unreadMessagesCount = action.count)
            }
            is Route.Chat -> null.also {
                action.apply {
                    accountId = chat.account.id
                    chatAddress = chat.address.id
                }.let(navigate)
            }
            is GetState -> this
            else -> null
        }
    }

    internal class Factory(
        private val latestMessageFlow: LatestMessageFlowSelector,
        private val presenceSelector: PresenceFlowSelector,
        private val navigate: Route.Navigate,
        private val messageRepo: Message.Repo
    ) : (Chat) -> RosterItemService by { chat ->
        RosterItemService(
            chat = chat,
            latestMessageFlow = latestMessageFlow,
            presenceOf = presenceSelector,
            navigate = navigate,
            messageRepo = messageRepo
        )
    }
}
