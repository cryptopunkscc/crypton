package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetChatInfo() = handle { out, _: Get.ChatInfo ->
    out(getChatInfo(chat.address))
}
