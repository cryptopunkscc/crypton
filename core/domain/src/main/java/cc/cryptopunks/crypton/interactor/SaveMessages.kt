package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.Chat
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.context.account
import cc.cryptopunks.crypton.context.downloadFile
import cc.cryptopunks.crypton.context.fileExtension
import cc.cryptopunks.crypton.context.getFile
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.context.parseUriData
import cc.cryptopunks.crypton.util.logger.log

internal suspend fun SessionScope.saveMessage(message: Message) {
    message
        .run { get(message) ?: create(message) }
        .also {
            try {
                if (
                    message.type == Message.Type.Url &&
                    message.body.isImageUrl() &&
                    !getFile(message.body).exists()
                ) {
                    downloadFile(message.body)
                }
            } catch (e: Throwable) {
                log.e { e }
            }
        }
        .let { prepared ->
            messageRepo.run {
                log.d { "inserting message $message" }

                insertOrUpdate(prepared)

                if (prepared.status == Message.Status.Received)
                    notifyUnread()
            }
        }
}

private fun String.isImageUrl() = parseUriData().fileExtension in imageExtensions

private val imageExtensions = listOf("png", "jpg", "jpeg")

private suspend fun SessionScope.get(message: Message) = messageRepo.run {
    get(message.id)?.also {
        delete(it)
    }?.copy(
        status = message.status
    )
}

private suspend fun SessionScope.create(message: Message) = message.copy(
    chat = Chat(
        account = account.address,
        address = message.chat
    ).also {
        insertChat(it)
    }.address
)
