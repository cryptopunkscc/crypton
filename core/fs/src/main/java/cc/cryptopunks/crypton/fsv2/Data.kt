package cc.cryptopunks.crypton.fsv2

typealias PubKey = String
typealias PubKeyFingerprint = String

data class Request(
    val id: PubKeyFingerprint,
    val signature: String,
    val data: Any
)

