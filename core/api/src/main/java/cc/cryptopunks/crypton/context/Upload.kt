package cc.cryptopunks.crypton.context

import kotlinx.coroutines.flow.Flow
import java.io.File
import java.net.URL

object Upload {

    data class Progress(
        val file: String,
        val totalBytes: Long = 0,
        val uploadedBytes: Long = 0,
        val url: URL? = null
    )

    interface Net {
        fun upload(file: File): Flow<Progress>
    }
}
