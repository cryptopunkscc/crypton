package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.Connector
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.service.start
import cc.cryptopunks.crypton.util.OpenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.random.Random

typealias Broadcast = Connector
typealias Connections = Flow<Connector>

fun connections(): Connections = emptyFlow()

val ports = 64000 .. 65000

fun randomPort() = ports.random()

fun createIdentity(
    port: Int = randomPort()
) = Identity(
    id = Random.nextBytes(32).decodeToString(),
    endpoint = "tcp:\\localhost:$port"
)

const val helloMessage = "Hello agent!!!"

fun CoroutineScope.startAgent(
    broadcast: Broadcast,
//    connections: Connections,
) = launch(
    cryptonContext(
        Dispatchers.IO,
        SupervisorJob(),
        handlers()
    )
) {
    joinAll(
        launch { broadcast.start() },
//        launch { connections.start() },
        launch {
            delay(1000)
            String()
            broadcast.output(helloMessage.toByteArray())
        }
    )
}

val CoroutineScope.ownIdentity: Identity by dep()
val CoroutineScope.identityStore: Identity.Store by dep()

data class Identity(
    val id: String,
    val endpoint: String,
    val services: Set<Class<out Action>> = emptySet()
) {
    class Store : OpenStore<Identities>(emptySet())
}

typealias Identities = Set<Identity>

data class RequestConnection(val connector: Connector) : Action

fun handlers() = cryptonContext(

    handler { _, (connector): RequestConnection ->
        connector.start()
    },

    handler { out, identity: Identity ->
        identityStore { plus(identity) }
        ownIdentity.out()
    }
)
