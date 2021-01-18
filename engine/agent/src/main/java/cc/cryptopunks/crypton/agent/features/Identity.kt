package cc.cryptopunks.crypton.agent.features

import cc.cryptopunks.crypton.Action
import cc.cryptopunks.crypton.agent.Identity
import cc.cryptopunks.crypton.agent.MemoryIdentityGraph
import cc.cryptopunks.crypton.agent.Socket
import cc.cryptopunks.crypton.agent.broadcast
import cc.cryptopunks.crypton.agent.connections
import cc.cryptopunks.crypton.agent.decode
import cc.cryptopunks.crypton.agent.encode
import cc.cryptopunks.crypton.agent.identityGraph
import cc.cryptopunks.crypton.agent.ownIdentity
import cc.cryptopunks.crypton.create.cryptonContext
import cc.cryptopunks.crypton.create.dep
import cc.cryptopunks.crypton.create.depKey
import cc.cryptopunks.crypton.create.emitter
import cc.cryptopunks.crypton.create.handler
import cc.cryptopunks.crypton.dependency.get
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

data class PostIdentity(
    val identity: Identity,
) : Action

private data class BroadcastIdentity(
    val identity: Identity,
) : Action

fun broadcastIdentity() = cryptonContext(

    MemoryIdentityGraph().dep<Identity.Graph>(),

    emitter { flowOf(BroadcastIdentity(ownIdentity)) },

    emitter {
        broadcast.input.map { it.decode() }
    },

    handler { _, (identity): BroadcastIdentity ->
        broadcast.output(PostIdentity(identity).encode())
    },

    handler { _, (identity): PostIdentity ->
        val ownIdentity = ownIdentity
        if (ownIdentity == identity) return@handler

        val currentSocket = get(depKey<Socket>())

        if (currentSocket != null && connections[identity.id] != currentSocket)
            connections[identity.id] = currentSocket

        identityGraph[identity] = ownIdentity
    }
)
