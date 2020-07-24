package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetJoinedRooms() = handle { out, _: Get.JoinedRooms ->
    out(Chat.Service.JoinedRooms(listJoinedRooms()))
}
