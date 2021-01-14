package cc.cryptopunks.crypton.fs.repo

import cc.cryptopunks.crypton.fs.Blob
import cc.cryptopunks.crypton.fs.Id
import java.io.File

class FileBlobRepo(
    dir: File
) : Blob.Repo {

    init {
        dir.mkdir()
    }

    private val blobStorage = FileStorage(dir.resolve(BLOB))

    override suspend fun plus(data: Blob): Id =
        data.apply {
            File(path).copyTo(
                target = blobStorage.new(id),
                overwrite = true
            )
        }.id

    override fun get(id: Id): Blob = blobStorage[id].run { Blob(id, absolutePath) }
    override fun contains(id: Id): Boolean = id in blobStorage
    override fun minus(id: Id): Boolean = blobStorage - id
    override fun typeOf(any: Any): Boolean = any is Blob

    private companion object {
        const val BLOB = "blob"
    }
}
