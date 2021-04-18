package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.agent.fake.fakeBroadcast
import cc.cryptopunks.crypton.agent.fake.fakeSocketConnections
import cc.cryptopunks.crypton.agent.fake.openFakeConnection
import cc.cryptopunks.crypton.agent.features.FILE_STORE
import cc.cryptopunks.crypton.agent.features.fileGraph
import cc.cryptopunks.crypton.agent.features.fileStore
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.fs.ormlite.OrmLiteGraph
import cc.cryptopunks.crypton.fsv2.FileStore
import cc.cryptopunks.crypton.fsv2.Graph
import cc.cryptopunks.crypton.util.ormlite.jvm.createJdbcH2ConnectionSource
import java.io.File

fun extendedContext(id: Int) = storeContext(id.toString())
//+ echo()

fun storeContext(name: String) = fakeContext() + cryptonContext(
    // dependencies
    OrmLiteGraph(createJdbcH2ConnectionSource(name = "graph_$name")).dep<Graph.ReadWrite>(),
    FileStore(File("store_$name")).dep(FILE_STORE),

    // features
    fileStore(),
    fileGraph(),
)

fun fakeContext() = cryptonContext(
    // dependencies
    fakeBroadcast().dep(BROADCAST),
    fakeSocketConnections.dep(SOCKET_CONNECTIONS),
    openFakeConnection.dep(OPEN_CONNECTION)
)
