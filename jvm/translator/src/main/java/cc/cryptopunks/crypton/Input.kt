package cc.cryptopunks.crypton

interface Input {
    val value: String?
    val isEmpty get() = value == null
    val isNotEmpty get() = isEmpty.not()

    data class Simple(
        override var value: String? = null
    ) : Input

    data class Vararg(
        override var value: String? = null
    ) : Input

    data class Named(
        val name: String,
        override var value: String? = null
    ) : Input
}

