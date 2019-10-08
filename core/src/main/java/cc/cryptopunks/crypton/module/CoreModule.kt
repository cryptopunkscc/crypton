package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.component.ClientComponent
import cc.cryptopunks.crypton.component.CoreComponent
import cc.cryptopunks.crypton.component.RepoComponent
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scope

class CoreModule(
    private val executorsComponent: ExecutorsComponent,
    private val repoComponent: RepoComponent,
    private val clientComponent: ClientComponent
) :
    CoreComponent,
    ExecutorsComponent by executorsComponent,
    RepoComponent by repoComponent,
    ClientComponent by clientComponent,
    BroadcastError.Component by BroadcastError.Module() {

    override val useCaseScope = Scope.UseCase(broadcastError)
}