package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.delegate.dep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

val CoroutineScope.ownIdentity: Identity by dep()
val CoroutineScope.identityGraph: Identity.Graph by dep()

typealias IdentityId = String

data class Identity(
    val id: IdentityId,
    val endpoints: Set<String> = emptySet(),
    val services: Set<String> = emptySet(),
) : Story {
    override val type: String get() = TYPE
    override val ver: Int = 0
    override val rel: List<String> = emptyList()

    companion object {
        const val TYPE = "identity"
    }

    interface Graph {
        operator fun set(left: Identity, right: Identity)
        fun path(from: IdentityId, to: IdentityId): List<Identity>
        fun newIdentities(): Flow<Identity>
        fun list(): Set<Identity>
    }
}
