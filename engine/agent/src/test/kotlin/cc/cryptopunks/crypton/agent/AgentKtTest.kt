package cc.cryptopunks.crypton.agent

import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AgentKtTest {

    @Test
    fun test() {
        runBlocking {
            joinAll(
                startAgent(loopback()),
                startAgent(loopback()),
            )
        }
    }
}
