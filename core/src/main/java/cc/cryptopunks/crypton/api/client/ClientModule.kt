package cc.cryptopunks.crypton.api.client

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.MapException

class ClientModule(
    createClient: Client.Factory,
    override val mapException: MapException,
    override val currentClient: Client.Current = Client.Current(),
    override val clientManager: Client.Manager = ClientManager(
        createClient = createClient,
        clientCache = ClientCache()
    )
) : Client.Component