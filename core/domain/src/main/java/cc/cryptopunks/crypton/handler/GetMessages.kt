package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.handle

internal fun handleGetMessages() = handle { out, _: Get.Messages ->
    messageRepo.list(
        chat = chat.address,
        range = System.currentTimeMillis().let { currentTime ->
            currentTime - SEVEN_DAYS_MILLIS..currentTime
        }
    ).forEach {
        out(Chat.Messages(address, listOf(it)))
    }
}

const val SEVEN_DAYS_MILLIS = 1000 * 60 * 60 * 24 * 7
