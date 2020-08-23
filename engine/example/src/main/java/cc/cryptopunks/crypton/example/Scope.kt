package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Scope

internal interface RootScope : Scope {
    val entityRepo: Entity.Repo
    val nestedScopes: Map<String, NestedScope>
}

internal interface NestedScope : RootScope {
    val id: String
}
