package cc.cryptopunks.crypton.core

import cc.cryptopunks.crypton.net.Net
import cc.cryptopunks.crypton.entity.Account
import cc.cryptopunks.crypton.presentation.PresentationManager
import cc.cryptopunks.crypton.repo.Repo
import cc.cryptopunks.crypton.service.Service
import cc.cryptopunks.crypton.sys.Sys
import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.ExecutorsComponent

object Core {

    interface Component :
        ExecutorsComponent,
        BroadcastError.Component,
        Net.Component,
        Repo.Component,
        Repo.Provider,
        Sys.Component {

        val serviceScope: Service.Scope
        val presentationManager: PresentationManager
        val accountNetManager: Account.Net.Manager
    }
}