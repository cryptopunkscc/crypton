package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

fun SessionScope.joinConferencesFlow() =
    accountAuthenticatedFlow().take(1).flatMapMerge {
        chatRepo.flowList()
    }.bufferedThrottle(300).flatMapConcat { list ->
        list.asSequence()
            .flatten()
            .filter(Chat::isConference)
            .map(Chat::address)
            .toSet()
            .minus(listJoinedRooms())
            .asFlow()
    }.map { chat ->
        Roster.Service.Join(address, chat)
    }
