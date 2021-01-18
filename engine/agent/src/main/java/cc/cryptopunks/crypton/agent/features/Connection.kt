package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.Async
import cc.cryptopunks.crypton.agent.Connections
import cc.cryptopunks.crypton.agent.Identity
import cc.cryptopunks.crypton.agent.Socket
import cc.cryptopunks.crypton.agent.connections
import cc.cryptopunks.crypton.agent.encode
import cc.cryptopunks.crypton.agent.identityGraph
import cc.cryptopunks.crypton.agent.openConnection
import cc.cryptopunks.crypton.agent.ownIdentity
import cc.cryptopunks.crypton.agent.socketConnections
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.service.start
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private data class OpenConnection(val identity: Identity) : Async
private data class IncomingConnection(val socket: Socket) : Async


fun handleConnections() = cryptonContext(

    Connections().dep(),

    emitter {
        val ownId = ownIdentity.id
        identityGraph.newIdentities()
            .filter { it.id != ownId }
            .map { OpenConnection(it) }
    },

    emitter {
        ownIdentity.endpoints.first()
            .let(socketConnections)
            .map { socket -> IncomingConnection(socket) }
    },

    handler { _, (identity): OpenConnection ->
        val connections = connections

        if (connections[identity.id] != null) return@handler

        val endpoint = identity.endpoints.first()
        val socket = openConnection(endpoint)
        connections[identity.id] = socket
        socket.output(PostIdentity(ownIdentity).encode())
        socket.start()
    },

    handler { _, (socket): IncomingConnection ->
        withContext(socket.dep()) {
            socket.start()
        }
    }
)
