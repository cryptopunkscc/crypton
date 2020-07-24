package cc.cryptopunks.crypton.example

import cc.cryptopunks.crypton.Scope

internal interface AppScope : Scope {
    val entityRepo: Entity.Repo
    val nestedScopes: Map<String, NestedScope>
}

internal interface NestedScope : AppScope {
    val id: String
}
