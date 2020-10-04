package cc.cryptopunks.crypton.handler

import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.handle
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce

internal fun handleUploadFile() = handle { _, (uri): Exec.Upload ->
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
