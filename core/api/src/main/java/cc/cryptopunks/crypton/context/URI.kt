package cc.cryptopunks.crypton.context

import java.io.File

data class URI(
    val path: String
) {

    interface Sys {
        fun resolve(uri: URI): File
    }
}
