package cc.cryptopunks.crypton.component

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.MapException

interface ClientComponent {
    val mapException: MapException
    val currentClient: Client.Current
    val clientManager: Client.Manager
}