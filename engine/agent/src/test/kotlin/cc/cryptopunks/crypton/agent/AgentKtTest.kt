package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.agent.features.echo
import cc.cryptopunks.crypton.gson.initGsonTransformations
import cc.cryptopunks.crypton.initJvmLog
import cc.cryptopunks.crypton.log.printLast
import cc.cryptopunks.crypton.util.ormlite.jvm.setOrmLiteLogLevel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AgentKtTest {

    init {
        initGsonTransformations()
    }

    @Test
    fun test() {
        runBlocking(Dispatchers.IO) {
            initJvmLog(printLast)
            setOrmLiteLogLevel()
            (0..2).map {
//                delay(1000)
                startAgent(extendedContext(it) + echo())
//                startAgent(
//                    if (it % 3 != 0) fakeContext()
//                    else extendedContext(it)
//                )
            }.joinAll()
        }
    }
}
