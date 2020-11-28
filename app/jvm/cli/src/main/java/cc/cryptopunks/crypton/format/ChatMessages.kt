package cc.cryptopunks.crypton.format

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import java.util.*

fun Chat.Messages.format(): String =
    list.joinToString("\n", transform = Message::format)

fun Message.format(): String =
    "${Date(timestamp)} $chat - $from ($status): $body"
