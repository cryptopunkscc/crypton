package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.URI
import java.io.File

class MockURISys : URI.Sys {
    override fun resolve(uri: URI): File {
        TODO("Not yet implemented")
    }
}
