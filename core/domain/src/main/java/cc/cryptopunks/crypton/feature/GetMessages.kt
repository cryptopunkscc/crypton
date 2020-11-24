package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Get
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext

internal fun getMessages() = feature(

    command = command(
        config("account"),
        config("chat"),
        name = "messages",
        description = "Get chat messages."
    ) { (account, chat) ->
        Get.Messages.inContext(account, chat)
    },

    handler = { out, _: Get.Messages ->
        messageRepo.list(
            chat = chat.address,
            range = System.currentTimeMillis().let { currentTime ->
                currentTime - SEVEN_DAYS_MILLIS..currentTime
            }
        ).forEach {
            out(Chat.Messages(address, listOf(it)))
        }
    }
)

const val SEVEN_DAYS_MILLIS = 1000 * 60 * 60 * 24 * 7
