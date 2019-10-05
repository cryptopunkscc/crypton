package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent
import cc.cryptopunks.crypton.util.Scopes

interface CoreComponent :
    ExecutorsComponent,
    BroadcastError.Component,
    Client.Component,
    Repo.Component {

    val useCaseScope: Scopes.UseCase
}