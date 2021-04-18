package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.agent.Identity
import cc.cryptopunks.crypton.agent.connections
import cc.cryptopunks.crypton.agent.encodeDatagram
import cc.cryptopunks.crypton.agent.encodeYamlWithType
import cc.cryptopunks.crypton.agent.identityGraph
import cc.cryptopunks.crypton.agent.ownIdentity
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.logv2.d
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

object SendEcho : Action

data class Echo(
    val identity: Identity,
    val timestamp: Long = System.currentTimeMillis(),
) : Action

fun echo() = cryptonContext(

    emitter {
        flow {
            while (true) {
                delay(5000)
                emit(SendEcho)
            }
        }
    },

    handler { _, _: SendEcho ->
        val echo = Echo(ownIdentity)
        val type = echo.javaClass.name
        val encoded = echo.encodeDatagram()
        val identityGraph = identityGraph
        val connections = connections

        identityGraph.list()
            .filter { it.services.contains(type) }
            .mapNotNull { connections[it.id] }
            .forEach { it.output(encoded) }
    },

    handler { _, action: Echo ->
        val id = fileStore + flowOf(action.identity.encodeYamlWithType())
        log.d { "${ownIdentity.id} $action $id" }
    }
)
