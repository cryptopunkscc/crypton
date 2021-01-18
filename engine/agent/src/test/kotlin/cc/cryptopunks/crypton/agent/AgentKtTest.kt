package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.agent.features.FILE_STORE
import cc.cryptopunks.crypton.agent.features.echo
import cc.cryptopunks.crypton.agent.features.fileGraph
import cc.cryptopunks.crypton.agent.features.fileStore
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.fs.ormlite.OrmLiteGraph
import cc.cryptopunks.crypton.fsv2.FileStore
import cc.cryptopunks.crypton.fsv2.Graph
import cc.cryptopunks.crypton.initJvmLog
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File
import kotlin.coroutines.EmptyCoroutineContext

class AgentKtTest {

    @Test
    fun test() {
        runBlocking(Dispatchers.IO) {
            initJvmLog()
            (0..9).map {
                delay(1000)
                startAgent(
                    if (it % 3 != 0) EmptyCoroutineContext
                    else cryptonContext(
                        echo(),
                        OrmLiteGraph(createJdbcH2ConnectionSource(name = "graph_$it")).dep<Graph.ReadWrite>(),
                        FileStore(File("store_$it")).dep(FILE_STORE),
                        fileStore(),
                        fileGraph(),
                    )
                )
            }.joinAll()
        }
    }
}
