package cc.cryptopunks.crypton.fs.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Writer
import kotlin.random.Random

class FileStorage(
    val dir: File,
) {
    init {
        dir.mkdir()
    }

    suspend fun new(name: String = randomName(), write: (Writer.() -> Unit)? = null): File =
        withContext(Dispatchers.IO) {
            dir.resolve(name).apply {
                if (!exists()) createNewFile()
                if (write != null) writer().use { writer ->
                    writer.write()
                    writer.flush()
                }
            }
        }

    operator fun get(name: String): File = dir.resolve(name)
    operator fun contains(name: String): Boolean = dir.resolve(name).run { exists() || isFile }
    operator fun minus(name: String): Boolean = dir.resolve(name).delete()

    private companion object {
        private fun randomName() = (System.nanoTime() + Random.nextLong()).toString()
    }
}

