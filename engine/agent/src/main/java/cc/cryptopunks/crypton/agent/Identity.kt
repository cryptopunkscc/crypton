package cc.cryptopunks.crypton.agent

import cc.cryptopunks.crypton.delegate.dep
import cc.cryptopunks.crypton.json.formatJson
import cc.cryptopunks.crypton.json.parseJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

val CoroutineScope.ownIdentity: Identity by dep()
val CoroutineScope.identityGraph: Identity.Graph by dep()

typealias IdentityId = String

@Serializable
data class Identity(
    val id: IdentityId,
    val endpoints: Set<String> = emptySet(),
    val services: Set<String> = emptySet(),
) {
    interface Graph {
        operator fun set(left: Identity, right: Identity)
        fun path(from: IdentityId, to: IdentityId): List<Identity>
        fun newIdentities(): Flow<Identity>
        fun list(): Set<Identity>
    }
}

private val CHARSET = Charsets.UTF_8

fun Any.encode(): ByteArray = "${javaClass.name}:${formatJson()}".toByteArray(CHARSET)

fun ByteArray.decode(): Any = toString(CHARSET).split(':', limit = 2).run {
    last().parseJson(Class.forName(first()))
}
