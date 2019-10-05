package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scopes

interface CoreComponent :
    ExecutorsComponent,
    BroadcastError.Component,
    ClientComponent,
    RepoComponent {

    val useCaseScope: Scopes.UseCase
}