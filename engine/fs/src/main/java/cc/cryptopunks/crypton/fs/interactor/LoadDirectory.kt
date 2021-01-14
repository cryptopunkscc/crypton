package cc.cryptopunks.crypton.fs.interactor

import cc.cryptopunks.crypton.fs.Path
import cc.cryptopunks.crypton.fs.Repository
import cc.cryptopunks.crypton.fs.util.blob
import cc.cryptopunks.crypton.fs.util.recursiveFilesFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapNotNull
import java.io.File

suspend fun Repository.loadDirectory(
    directoryPath: Path
) {
    File(directoryPath).recursiveFilesFlow().mapNotNull { file ->
        if (!parse.can(file)) null
        else {
            val blob = file.blob()
            val rel = setOf(blob.id)
            blob to parse(file).map { lore -> lore.copy(rel = rel) }
        }
    }.collect { (blob, list) ->
        blobRepo + blob
        list.forEach { lore -> loreRepo + lore }
    }
}
