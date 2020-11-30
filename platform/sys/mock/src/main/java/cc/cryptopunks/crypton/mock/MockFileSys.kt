package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.File
import cc.cryptopunks.crypton.context.JavaFile

object MockFileSys : File.Sys {

    override fun filesDir(): JavaFile {
        TODO("Not yet implemented")
    }

    override fun cacheDir(): JavaFile {
        TODO("Not yet implemented")
    }

    override fun tmpDir(): JavaFile {
        TODO("Not yet implemented")
    }
}
