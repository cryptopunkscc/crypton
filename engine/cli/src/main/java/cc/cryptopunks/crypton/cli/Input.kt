package cc.cryptopunks.crypton.cli

interface Input {
    var value: String?
    val isEmpty get() = value == null
    val isNotEmpty get() = isEmpty.not()

    data class Param(
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

fun param() = Input.Param()
fun vararg() = Input.Vararg()
fun named(name: String) = Input.Named(name)
