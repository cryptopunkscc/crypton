package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleListRooms() = handle { out, _: Chat.Service.ListRooms ->
    out(Chat.Service.AllRooms(listRooms().toSet()))
}
