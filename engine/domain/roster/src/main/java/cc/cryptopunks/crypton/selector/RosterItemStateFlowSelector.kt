package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.*
import kotlinx.coroutines.flow.*

internal class RosterItemStateFlowSelector(
    private val presenceFlow: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector,
    private val messageRepo: Message.Repo
) {

    operator fun invoke(chatAddress: Address): Flow<Roster.Item.State> = run {
        flowOf(
            flowOf(Unit),
            presenceFlow(chatAddress),
            latestMessageFlow(chatAddress),
            messageRepo.unreadCountFlow(chatAddress)
        ).flattenMerge().scan(
            Roster.Item.State(
                letter = chatAddress.toString().firstOrNull()?.toLowerCase() ?: 'a',
                title = chatAddress.toString()
            )
        ) { item, changed ->
            when (changed) {
                is Presence.Status -> item.copy(presence = changed)
                is Message -> item.copy(message = changed)
                is Int -> item.copy(unreadMessagesCount = changed)
                else -> item
            }
        }
    }

    class Factory(
        private val presenceFlow: PresenceFlowSelector
    ) {
        operator fun invoke(session: Session) =
            RosterItemStateFlowSelector(
                presenceFlow = presenceFlow,
                latestMessageFlow = LatestMessageFlowSelector(
                    session.messageRepo
                ),
                messageRepo = session.messageRepo
            )
    }
}
