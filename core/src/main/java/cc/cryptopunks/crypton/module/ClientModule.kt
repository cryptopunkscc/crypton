package cc.cryptopunks.crypton.module

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.MapException
import cc.cryptopunks.crypton.component.ClientComponent

class ClientModule(
    override val createClient: Client.Factory,
    override val clientCache: Client.Cache = Client.Cache(),
    override val mapException: MapException
) : ClientComponent