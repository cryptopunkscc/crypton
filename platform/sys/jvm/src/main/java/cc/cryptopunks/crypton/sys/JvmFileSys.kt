package cc.cryptopunks.crypton.sys

import cc.cryptopunks.crypton.context.File
import cc.cryptopunks.crypton.context.JavaFile

internal class JvmFileSys(
    private val home: JavaFile
) : File.Sys {
    override fun filesDir(): JavaFile = home.resolve("files").apply { mkdir() }
    override fun cacheDir(): JavaFile = home.resolve("cache").apply { mkdir() }
    override fun tmpDir(): JavaFile = home.resolve("tmp").apply { mkdir() }
    override fun imageSizeOf(image: JavaFile): Pair<Int, Int> = unknownSize
}

private val unknownSize = 0 to 0
