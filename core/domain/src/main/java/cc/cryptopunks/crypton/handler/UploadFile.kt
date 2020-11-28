package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

fun uploadFile() = feature(

    command(
        config("account"),
        config("chat"),
        param().copy(name = "file", description = "Path to the file for upload."),
        name = "upload file",
        description = "Upload file to server and share link in chat."
    ),

    handler = { _, (uri): Exec.Upload ->
        val file = uriSys.resolve(uri)
        log.d { file }
        upload(file).debounce(1000)
//        .scan(Message()) { message, progress ->
//            progress.run {
//                message.copy(
//                    body = Message.Text("$uploadedBytes/$totalBytes $url")
//                )
//            }
//        }
            .collect {
                log.d { it }
//            messageRepo.insertOrUpdate(it)
            }
    }
)
