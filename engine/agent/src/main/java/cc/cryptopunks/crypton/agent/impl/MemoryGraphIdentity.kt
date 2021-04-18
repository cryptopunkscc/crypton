package cc.cryptopunks.crypton.agent.impl

import cc.cryptopunks.crypton.agent.Identity
import cc.cryptopunks.crypton.agent.IdentityId
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class MemoryGraphIdentity : Identity.Graph {

    private val vertices: IdentityVertices = mutableMapOf()
    private val edges: IdentityEdges = mutableMapOf()
    private val channel = BroadcastChannel<Identity>(Channel.BUFFERED)

    override fun set(left: Identity, right: Identity) {
        edges.set(left, right)
        vertices.set(left, right).forEach { added ->
            channel.offer(added)
        }
    }

    override fun list(): Set<Identity> = vertices.values.toSet()

    override fun path(from: IdentityId, to: IdentityId): List<Identity> =
        edges.path(from, to).map { vertices.getValue(it) }

    override fun newIdentities(): Flow<Identity> =
        channel.asFlow()
}

private typealias IdentityVertices = MutableMap<IdentityId, Identity>
private typealias IdentityEdges = MutableMap<IdentityId, Set<IdentityId>>

private fun IdentityVertices.set(vararg identities: Identity): List<Identity> =
    identities.mapNotNull { identity ->
        if (put(identity.id, identity) == identity) null
        else identity
    }

private fun IdentityEdges.set(left: Identity, right: Identity) {
    put(left.id, getOrElse(left.id) { emptySet() } + right.id)
}

private fun IdentityEdges.path(
    from: IdentityId,
    to: IdentityId,
    ignore: Set<IdentityId> = emptySet(),
): List<IdentityId> {
    val neighbours = get(to) ?: emptySet()
    return when {
        neighbours.isEmpty() -> emptyList()
        neighbours.contains(from) -> listOf(from, to)
        else -> neighbours
            .minus(ignore)
            .map { neighbour -> path(from, neighbour, ignore + to) }
            .minByOrNull { it.size }
            ?: emptyList()
    }
}
