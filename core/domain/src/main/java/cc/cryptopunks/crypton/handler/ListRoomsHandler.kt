package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.AppScope
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun AppScope.handleListRooms() =
    handle<Chat.Service.ListRooms> { out ->
        sessions.get().values.run {
            if (accounts.isEmpty()) this
            else filter { scope -> accounts.contains(scope.address) }
        }.flatMap { scope ->
            scope.listRooms()
        }.let { rooms ->
            Chat.Service.AllRooms(rooms.toSet())
        }.let { all ->
            out(all)
        }
    }
