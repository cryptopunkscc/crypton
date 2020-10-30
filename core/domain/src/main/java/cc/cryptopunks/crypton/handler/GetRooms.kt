package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetHostedRooms() = handle { out, _: Get.HostedRooms ->
    Chat.AllRooms(listHostedRooms().toSet()).out()
}

internal fun handleGetJoinedRooms() = handle { out, _: Get.JoinedRooms ->
    Chat.JoinedRooms(listJoinedRooms()).out()
}
