package cc.cryptopunks.crypton.fs.util

import cc.cryptopunks.crypton.fs.Blob
import java.io.File

internal fun File.blob() = Blob(
    id = readId(),
    path = absolutePath
)
