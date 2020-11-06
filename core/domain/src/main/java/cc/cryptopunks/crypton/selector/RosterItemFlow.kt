package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Presence
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.scan

internal fun SessionScope.rosterItemStatesFlow(chatAddress: Address): Flow<Roster.Item> =
    flowOf(
        flowOf(Unit),
        presenceFlow(chatAddress),
        latestMessageFlow(chatAddress),
        messageRepo.flowUnreadCount(chatAddress)
    ).flattenMerge().scan(
        Roster.Item(
            account = address,
            chatAddress = chatAddress,
            letter = chatAddress.toString().firstOrNull()?.toLowerCase() ?: 'a',
            title = chatAddress.toString(),
            updatedAt = System.currentTimeMillis()
        )
    ) { item, changed ->
        when (changed) {
            is Presence.Status -> item.copy(presence = changed)
            is Message -> item.copy(message = changed)
            is Int -> item.copy(unreadMessagesCount = changed)
            else -> null
        }
            ?.copy(updatedAt = System.currentTimeMillis())
            ?: item
    }
