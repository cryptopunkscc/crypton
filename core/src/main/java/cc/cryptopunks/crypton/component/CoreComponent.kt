package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scope

interface CoreComponent :
    ExecutorsComponent,
    BroadcastError.Component,
    ClientComponent,
    RepoComponent {

    val useCaseScope: Scope.UseCase
}