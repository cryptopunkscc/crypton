package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

interface CoreComponent :
    ExecutorsComponent,
    BroadcastError.Component,
    ClientComponent,
    RepoComponent {

    val serviceScope: Service.Scope
}