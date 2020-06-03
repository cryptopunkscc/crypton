package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.scan

internal class RosterItemStateFlowSelector(
    private val presenceFlow: PresenceFlowSelector,
    private val latestMessageFlow: LatestMessageFlowSelector,
    private val messageRepo: Message.Repo
) {

    operator fun invoke(chatAddress: Address): Flow<Roster.Item.Chat> = run {
        flowOf(
            flowOf(Unit),
            presenceFlow(chatAddress),
            latestMessageFlow(chatAddress),
            messageRepo.unreadCountFlow(chatAddress)
        ).flattenMerge().scan(
            Roster.Item.Chat(
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
