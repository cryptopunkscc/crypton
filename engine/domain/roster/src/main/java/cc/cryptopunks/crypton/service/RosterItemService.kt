package cc.cryptopunks.crypton.service

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.selector.LatestMessageFlowSelector
import cc.cryptopunks.crypton.selector.PresenceFlowSelector
import cc.cryptopunks.crypton.util.Store
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RosterItemService private constructor(
    private val chat: Chat,
    private val navigate: Route.Navigate,
    private val presenceOf: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector,
    private val messageRepo: Message.Repo
) : Roster.Item.Service {

    private data class UnreadMessages(val count: Int)

    object GetState

    override val id get() = chat.address.id

    private val store = Store(
        Roster.Item.State(
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

    private suspend fun reduce(action: Any): Roster.Item.State? = store reduce {
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
