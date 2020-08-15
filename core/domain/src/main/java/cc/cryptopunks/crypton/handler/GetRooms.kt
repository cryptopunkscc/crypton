package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetRooms() = handle { out, _: Get.Rooms ->
    require(false)
    Chat.AllRooms(listRooms().toSet()).out()
}
