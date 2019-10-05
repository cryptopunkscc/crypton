package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.MapException

interface ClientComponent {
    val createClient: Client.Factory
    val clientCache: Client.Cache
    val mapException: MapException
}