package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Context
import cc.cryptopunks.crypton.Scope

internal interface RootScope : Scope {
    val entityRepo: Entity.Repo
    val nestedScopes: Map<String, NestedScope>
    suspend infix fun resolve(context: Context): Pair<Scope, Any> = this to Unit
}

internal interface NestedScope : RootScope {
    val id: String
}
