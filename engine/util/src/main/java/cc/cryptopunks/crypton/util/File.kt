package cc.cryptopunks.crypton.util

import java.io.File

fun File.rename(transform: (String) -> String): File =
    parentFile.resolve(transform(name)).also { renameTo(it) }
