package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.URI
import java.io.File
import java.io.InputStream

object MockURISys : URI.Sys {
    override fun resolve(uri: URI): File {
        TODO("Not yet implemented")
    }

    override fun inputStream(uri: URI): InputStream {
        TODO("Not yet implemented")
    }

    override fun getMimeType(uri: URI): String {
        TODO("Not yet implemented")
    }
}
