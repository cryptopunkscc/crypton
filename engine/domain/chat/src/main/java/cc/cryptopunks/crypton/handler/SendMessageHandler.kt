package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.module.handle
import kotlinx.coroutines.launch
import java.security.MessageDigest

internal fun Session.handleSendMessage(
    chat: Chat
) = handle<Chat.Service.SendMessage> {
    scope.launch {
        messageRepo.insertOrUpdate(
            chat.createQueuedMessage(text)
        )
    }
}

private fun Chat.createQueuedMessage(text: String) =
    System.currentTimeMillis().let { timestamp ->
        Message(
            id = (text + address + account + timestamp).md5(),
            text = text,
            from = Resource(account),
            to = Resource(address),
            status = Message.Status.Queued,
            chatAddress = address,
            timestamp = timestamp
        )
    }

private fun String.md5() = MD5.digest(toByteArray()).printHexBinary()

private val MD5 = MessageDigest.getInstance("MD5")

private fun ByteArray.printHexBinary(): String =
    asSequence().map(Byte::toInt).fold(StringBuilder(size * 2)) { acc, i ->
        acc.append(HEX_CHARS[i shr 4 and 0xF]).append(HEX_CHARS[i and 0xF])
    }.toString()

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
