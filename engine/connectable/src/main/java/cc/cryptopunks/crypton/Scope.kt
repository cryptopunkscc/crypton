package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope

interface Scope : CoroutineScope {
    val handlers: HandlerRegistry
    val resolvers: Resolvers
    suspend infix fun resolve(context: Context): Pair<Scope, Any> = this to Unit 
}
