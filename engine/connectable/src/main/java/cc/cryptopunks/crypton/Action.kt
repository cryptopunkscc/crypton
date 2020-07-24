package cc.cryptopunks.crypton

interface Action {
    data class Error(
        val message: String?,
        val action: String
    )
}

interface Async : Action

interface Subscription : Action {
    val enable: Boolean
}
