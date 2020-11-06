package cc.cryptopunks.crypton.fs.parser

import cc.cryptopunks.crypton.fs.Repository
import cc.cryptopunks.crypton.fs.interactor.loadDirectory
import cc.cryptopunks.crypton.fs.ormlite.OrmLiteLoreRepo
import cc.cryptopunks.crypton.fs.parser.mp3.TYPE_ID3V1
import cc.cryptopunks.crypton.fs.parser.mp3.TYPE_ID3V2
import cc.cryptopunks.crypton.fs.parser.mp3.TYPE_MP3
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import java.io.File

class LoadDirectoryKtTest {

    private val dir = File("test_storage")
    private val src = System.getenv("HOME") + "/Muzyka"

    @After
    fun tearDown() {
//        dir.deleteRecursively()
    }

    @Test
    fun testLoreStorage() {
        runBlocking {
            Repository(dir).testLoadDirectory()
        }
    }

    @Test
    fun testOrmLiteLoreRepo() {
        runBlocking {
            val repo = OrmLiteLoreRepo(createJdbcH2ConnectionSource(
                dir.resolve("lore").canonicalPath
            ))
            Repository(
                dir = dir,
                loreRepo = repo
            ).testLoadDirectory()
            repo.close()
        }
    }

    private suspend fun Repository.testLoadDirectory() {
        println(dir.absolutePath)
        loadDirectory(src)
        println("===============================")
        getByType(setOf(TYPE_ID3V1, TYPE_ID3V2, TYPE_MP3)).forEach(::println)
    }
}
