package cc.cryptopunks.crypton.interactor

import cc.cryptopunks.crypton.context.AesGcm
import cc.cryptopunks.crypton.context.Crypto
import cc.cryptopunks.crypton.context.RootScope
import cc.cryptopunks.crypton.factory.decodeAesGcmUrl
import cc.cryptopunks.crypton.util.useCopyTo
import java.io.File
import java.io.InputStream
import java.net.URL

internal fun RootScope.downloadFile(url: String): File =
    when (url.split(":").first()) {
        "http",
        "https" -> downloadHttpFile(url)
        "aesgcm" -> downloadAesGcmFile(url.decodeAesGcmUrl())
        else -> throw IllegalArgumentException("Cannot handle scheme of: $url")
    }

internal fun RootScope.downloadHttpFile(url: String): File =
    newFile(url).apply {
        url.openStream().useCopyTo(outputStream())
    }

internal fun RootScope.downloadAesGcmFile(
    link: AesGcm.Link
): File = newFile(link.url.split("/").last()).apply {
    link.url.openStream().useCopyTo(
        cryptoSys.transform(
            stream = outputStream(),
            secure = link.secure,
            mode = Crypto.Mode.Decrypt
        )
    )
}

private fun String.openStream(): InputStream =
    try {
        URL(this).openStream()
    } catch (e: Throwable) {
        throw Exception(e.message + ": " + this, e.cause)
    }

private fun RootScope.newFile(name: String) =
    fileSys.filesDir().resolve(name)
