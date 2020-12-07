package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.useCopyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL

suspend fun RootScope.downloadFile(url: String): JavaFile = withContext(Dispatchers.IO) {
    when (url.split(":").first()) {
        "http",
        "https",
        -> downloadHttpFile(url)
        "aesgcm" -> downloadAesGcmFile(url.decodeAesGcmUrl())
        else -> throw IllegalArgumentException("Cannot handle scheme of: $url")
    }
}

fun RootScope.getFile(url: String): JavaFile =
    fileSys.filesDir().resolve(url.parseUriData().fileName)

private fun RootScope.downloadHttpFile(url: String): JavaFile =
    getFileFromPath(url).apply {
        url.openStream().useCopyTo(outputStream())
    }

private fun RootScope.downloadAesGcmFile(
    link: AesGcm.Link,
): JavaFile = getFileFromPath(link.url).apply {
    link.url.openStream().useCopyTo(
        cryptoSys.transform(
            stream = outputStream(),
            secure = link.secure,
            mode = Crypto.Mode.Decrypt
        )
    )
}

private fun String.openStream(): InputStream = URL(this).openStream()

internal fun RootScope.getFileFromPath(path: String) =
    fileSys.filesDir().resolve(path.parseUriData().fileName!!)

