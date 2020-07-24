package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.handle

internal fun handleListJoinedRooms() = handle { out, _: Chat.Service.ListJoinedRooms ->
    out(Chat.Service.JoinedRooms(listJoinedRooms()))
}
