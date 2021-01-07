package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.net.URL

val SessionScope.uploadNet: Upload.Net by dep()

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
