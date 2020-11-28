package cc.cryptopunks.crypton

import java.io.File

internal fun main(args: Array<String>) {
    println(File("").absolutePath)
    embeddedServer(args).invoke()
}
