package cc.cryptopunks.crypton.context

object Api {
    interface Event

    data class Error(
        val message: String?,
        val command: String
    )
}
