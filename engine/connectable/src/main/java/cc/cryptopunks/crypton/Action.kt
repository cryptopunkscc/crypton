package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope

interface Action {
    data class Error(
        val message: String?,
        val action: String
    )
}

interface Async : Action

interface Subscription : Action {
    val enable: Boolean get() = true
}

interface Scoped<S : CoroutineScope> : Action {
    data class Resolved(
        val scope: Scope,
        val action: Any
    )
}
