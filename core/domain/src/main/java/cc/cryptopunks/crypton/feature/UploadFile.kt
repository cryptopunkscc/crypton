package cc.cryptopunks.crypton.feature

import cc.cryptopunks.crypton.cliv2.command
import cc.cryptopunks.crypton.cliv2.config
import cc.cryptopunks.crypton.cliv2.param
import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Message
import cc.cryptopunks.crypton.context.URI
import cc.cryptopunks.crypton.factory.createEmptyMessage
import cc.cryptopunks.crypton.feature
import cc.cryptopunks.crypton.inContext
import cc.cryptopunks.crypton.util.hexToBytes
import cc.cryptopunks.crypton.util.logger.log
import cc.cryptopunks.crypton.util.toHex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import java.io.FileInputStream

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
                outputStream().use { out ->
                    cryptoSys.encrypt(
                        inputStream = FileInputStream(file),
                        secure = secure
                    ).copyTo(out)
                }
            }
        }

        upload(encryptedFile).debounce(200)
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

                messageRepo.insertOrUpdate(
                    chat.createEmptyMessage().copy(
                        body = aesGcmUrl.encodeString(),
                        type = Message.Type.Url
                    )
                )
            }
    }
)

private const val HTTPS = "https"

private const val AES_GCM = "aesgcm"

private fun AesGcm.Link.encodeString(): String =
    listOf(
        "$AES_GCM:",
        url.removePrefix("$HTTPS:"),
        "#",
        secure.iv.toHex(),
        secure.key.toHex(),
    ).joinToString("")



private fun String.decodeAesGcmUrl(): AesGcm.Link = this
    .split(":|#")
    .let { (scheme, link, ivKey) ->
        require(scheme == AES_GCM)
        AesGcm.Link(
            url = "$HTTPS:$link",
            secure = AesGcm.Secure(
                key = ivKey.takeLast(64).hexToBytes(),
                iv = ivKey.dropLast(64).hexToBytes(),
            )
        )
    }
