package cc.cryptopunks.crypton

data class Scoped(
    val id: String,
    val next: Action,
) : Action
