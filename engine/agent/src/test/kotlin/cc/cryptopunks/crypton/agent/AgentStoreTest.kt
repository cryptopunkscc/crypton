package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.agent.features.GraphRequest
import cc.cryptopunks.crypton.agent.features.StoreRequest
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.initJvmLog
import cc.cryptopunks.crypton.json.initJacksonTransformations
import cc.cryptopunks.crypton.log.printGroup
import cc.cryptopunks.crypton.util.ormlite.jvm.setOrmLiteLogLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AgentStoreTest {

    init {
        initJacksonTransformations()
    }

    @Test
    fun test() {
        runBlocking {
            initJvmLog(printGroup)
            setOrmLiteLogLevel()
            joinAll(
                startAgent(storeContext("store")),
                launch {
                    delay(3000)
                    startAgent(testStore()).join()
                }
            )
        }
    }
}

private data class TestCommand(
    val identity: Identity,
    val actions: List<Action> = listOf(
//        StoreRequest.Add("Elo mordo!!!".toByteArray()),
//        StoreRequest.Add(TestStory().encodeYaml().toByteArray()),
        GraphRequest.AllStories,
    ),
) : Action

private data class TestStory(
    override val ver: Int = 0,
    override val type: String = "test",
    override val rel: List<String> = listOf("517A211D6895CB2E6AFC9F0FD6322FE3D344D248171A9FD3B243C1A974E63651"),
    val message: String = "Yolo",
) : Story

private fun testStore() = storeContext("test") + cryptonContext(

    emitter {
        identityGraph.newIdentities().map {
            delay(1000)
            TestCommand(it)
        }
    },

    handler { _, action: TestCommand ->
        connections[action.identity.id]?.run {
            action.actions.forEach {
                it.encodeDatagram().out()
            }
        }
    }
)
