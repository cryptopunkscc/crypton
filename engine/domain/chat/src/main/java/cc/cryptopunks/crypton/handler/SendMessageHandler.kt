package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.Resource
import cc.cryptopunks.crypton.context.Session
import cc.cryptopunks.crypton.context.handle
import cc.cryptopunks.crypton.util.TypedLog
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.launch
import java.security.MessageDigest

private class SendMessageHandler

internal fun Session.handleSendMessage(
    chat: Chat,
    log: TypedLog = SendMessageHandler().typedLog()
) = handle<Chat.Service.SendMessage> {
    scope.launch {
        chat.createQueuedMessage(text).let { message ->
            log.d("Enqueue message $message")
            messageRepo.insertOrUpdate(message)
        }
    }
}

private fun Chat.createQueuedMessage(text: String) =
    System.currentTimeMillis().let { timestamp ->
        Message(
            text = text,
            from = Resource(account),
            to = Resource(address),
            status = Message.Status.Queued,
            chatAddress = address,
            timestamp = timestamp
        ).calculateId()
    }

fun Message.calculateId() = copy(
    id = (text + from + to + timestamp).md5()
)

private fun String.md5() = MD5.digest(toByteArray()).printHexBinary()

private val MD5 = MessageDigest.getInstance("MD5")

private fun ByteArray.printHexBinary(): String =
    asSequence().map(Byte::toInt).fold(StringBuilder(size * 2)) { acc, i ->
        acc.append(HEX_CHARS[i shr 4 and 0xF]).append(HEX_CHARS[i and 0xF])
    }.toString()

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()
