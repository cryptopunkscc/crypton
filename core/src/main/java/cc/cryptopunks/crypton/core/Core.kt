package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

object Core {

    interface Component :
        ExecutorsComponent,
        BroadcastError.Component,
        Client.Component,
        Repo.Component {

        val serviceScope: Service.Scope
    }
}