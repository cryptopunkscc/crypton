package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun AppScope.handleListJoinedRooms() =
    handle<Chat.Service.ListJoinedRooms> { out ->
        out(
            Chat.Service.JoinedRooms(
                sessions[account]!!.listJoinedRooms()
            )
        )
    }
