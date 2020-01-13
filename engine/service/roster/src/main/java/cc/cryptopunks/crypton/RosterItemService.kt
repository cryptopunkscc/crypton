package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.selector.LatestMessageFlowSelector
import cc.cryptopunks.crypton.selector.PresenceFlowSelector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class RosterItemService private constructor(
    private val chat: Chat,
    private val navigate: Route.Api.Navigate,
    private val presenceOf: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector
) : Service {

    data class State(
        val title: String,
        val letter: Char,
        val message: Message = Message.Empty,
        val presence: Presence.Status = Presence.Status.Unavailable
    )

    override val id get() = chat.address.id

    override val coroutineContext = SupervisorJob() + Dispatchers.IO

    private var state = State(
        letter = id.firstOrNull()?.toLowerCase() ?: 'a',
        title = id
    )

    override fun Service.Binding.bind() = launch {
        state.out()
        flowOf(
            input,
            presenceOf(chat.address),
            latestMessageFlow(chat)
        ).flattenMerge().mapNotNull { action ->
            state.reduce(action)?.let { newState ->
                state = newState
                Service.Result(action, newState)
            }
        }.collect(output)
    }

    private fun State.reduce(action: Any): State? = when (action) {
        is Presence.Status -> copy(presence = action)
        is Message -> copy(message = action)
        is Route.Chat -> apply {
            action.apply {
                accountId = chat.account.id
                chatAddress = chat.address.id
            }.let(navigate)
        }
        else -> null
    }

    class Factory @Inject constructor(
        private val latestMessageFlow: LatestMessageFlowSelector,
        private val presenceSelector: PresenceFlowSelector,
        private val navigate: Route.Api.Navigate
    ) : (Chat) -> RosterItemService by { chat ->
        RosterItemService(
            chat = chat,
            latestMessageFlow = latestMessageFlow,
            presenceOf = presenceSelector,
            navigate = navigate
        )
    }
}