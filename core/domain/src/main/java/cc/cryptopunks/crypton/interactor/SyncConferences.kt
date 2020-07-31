package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.Output
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Address
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList


private const val MAX_ATTEMPTS = 5
private val initial = (0..1).toList()

private val fib = (0..MAX_ATTEMPTS - initial.size).fold(initial) { acc, _ ->
    acc.run { plus(takeLast(2).run { first() + last() }) }
}

internal suspend fun SessionScope.syncConferencesWithRetry(out: Output) {
    fib.withIndex().asFlow().map { (attempt, wait) ->
        delay(1500L * wait)
        log.d { "Syncing conferences attempt $attempt $wait" }
        listRooms()
    }.filter { it.isNotEmpty() }.firstOrNull()?.let { rooms ->
        log.d { "Fetched conferences $rooms" }
        syncConferences(rooms).map { it.address }.onEach { room ->
            out(Account.ChatCreated(room))
        }.toList().also { syncRooms ->
            log.d { "Conferences sync $syncRooms" }
        }
    }
    log.d { "Finish syncing conferences" }
}

private suspend fun SessionScope.syncConferences(list: Set<Address>) =
    chatRepo.list(listOf(address)).map(Chat::address).let { savedChats ->
        list - savedChats
    }.also {
        log.d { "Creating conferences $it" }
    }.map { chat ->
        Chat(
            title = chat.local,
            address = chat,
            account = address
        )
    }.asFlow().onEach { chat ->
        createChat(chat)
    }
