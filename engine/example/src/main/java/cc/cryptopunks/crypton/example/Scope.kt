package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Scope
import cc.cryptopunks.crypton.Scoped

internal interface RootScope : Scope {
    val entityRepo: Entity.Repo
    val nestedScopes: Map<String, NestedScope>
    suspend infix fun resolve(context: Scoped): Pair<Scope, Any> = this to Unit
}

internal interface NestedScope : RootScope {
    val id: String
}
