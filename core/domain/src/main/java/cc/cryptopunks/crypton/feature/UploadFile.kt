package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Crypto
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.factory.createEmptyMessage
import cc.cryptopunks.crypton.factory.encodeString
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.util.logger.log
import cc.cryptopunks.crypton.util.rename
import cc.cryptopunks.crypton.util.useCopyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

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

        val secure = AesGcm.Secure()

        val encryptedFile = withContext(Dispatchers.IO) {
            fileSys.tmpDir().resolve(file.name).apply {
                createNewFile()
                deleteOnExit()
                cryptoSys.transform(
                    stream = file.inputStream(),
                    secure = secure,
                    mode = Crypto.Mode.Encrypt
                ).useCopyTo(outputStream())
            }
        }

        upload(encryptedFile).debounce(100)
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
                val aesGcmUrl = AesGcm.Link(
                    url = result.url!!.toString(),
                    secure = secure
                )
                encryptedFile.rename { "$it.up" }
                messageRepo.insertOrUpdate(
                    chat.createEmptyMessage().copy(
                        body = aesGcmUrl.encodeString(),
                        type = Message.Type.Url
                    )
                )
            }
    }
)
