package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.URI
import java.io.File

object JvmUriSys : URI.Sys {
    override fun resolve(uri: URI): File = File(uri.path)
}
