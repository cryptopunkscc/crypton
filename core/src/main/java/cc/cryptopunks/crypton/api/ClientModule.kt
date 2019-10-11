package cc.cryptopunks.crypton.api

class ClientModule(
    createClient: Client.Factory,
    override val mapException: MapException,
    override val currentClient: Client.Current = Client.Current(),
    override val clientManager: Client.Manager = Client.Manager(
        createClient = createClient,
        clientCache = Client.Cache()
    )
) : Client.Component