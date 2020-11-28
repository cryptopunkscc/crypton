package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.factory.createMessage
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList

fun uploadFile() = feature(

    command(
        config("account"),
        config("chat"),
        param().copy(name = "file", description = "Path to the file for upload."),
        name = "upload file",
        description = "Upload file to server and share link in chat."
    ) { (account, chat, file) ->
        Exec.Upload(URI(file)).inContext(account, chat)
    },

    handler = { out, (uri): Exec.Upload ->
        val file = uriSys.resolve(uri)
        log.d { file }
        upload(file).debounce(200)
//        .scan(Message()) { message, progress ->
//            progress.run {
//                message.copy(
//                    body = Message.Text("$uploadedBytes/$totalBytes $url")
//                )
//            }
//        }
            .onEach {
                log.d { it }
                it.out()
//            messageRepo.insertOrUpdate(it)
            }
            .toList()
            .last()
            .let { result ->
                messageRepo.insertOrUpdate(
                    chat.createMessage().copy(
                        body = result.url!!.toString(),
                        type = Message.Type.Url
                    )
                )
            }
    }
)
