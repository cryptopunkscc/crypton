package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.URI
import java.io.File
import java.io.InputStream

object JvmUriSys : URI.Sys {
    override fun resolve(uri: URI): File = File(uri.path)

    override fun inputStream(uri: URI): InputStream = resolve(uri).inputStream()

    override fun getMimeType(uri: URI): String = "*/" + resolve(uri).extension
}
