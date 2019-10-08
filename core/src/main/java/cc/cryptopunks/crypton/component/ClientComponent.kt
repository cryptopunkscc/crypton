package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.MapException

interface ClientComponent {
    val mapException: MapException
    val createClient: Client.Factory
    val currentClient: Client.Current
    val clientCache: Client.Cache
    val clientRepo: Client.Repo
}