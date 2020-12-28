package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.factory.handler
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.context.downloadFile
import cc.cryptopunks.crypton.context.messageRepo
import cc.cryptopunks.crypton.util.rename

internal fun downloadFile() = feature(

    command = command(
        config("account"),
        param().copy(name = "messageId"),
        name = "download",
        description = "Download file from message with given id"
    ) { (account, id) ->
        Exec.DownloadFromMessage(id).inContext(account)
    },

    handler = handler {out, (messageId): Exec.DownloadFromMessage ->
        messageRepo.get(messageId)?.run {
            require(type == Message.Type.Url)
            downloadFile(url = body).rename { "$messageId-$it" }
        }?.let {
            URI(it.absolutePath).out()
        } ?: require(false) {
            "Cannot find message with id $messageId"
        }
    }
)
