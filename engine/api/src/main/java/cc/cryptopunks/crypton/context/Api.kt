package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.util.BroadcastError
import cc.cryptopunks.crypton.util.Executors

object Api {

    interface Event

    interface Core :
        Executors,
        BroadcastError.Core,
        Connection.Core,
        Sys,
        Repo {

        val serviceScope: Service.Scope
    }
}