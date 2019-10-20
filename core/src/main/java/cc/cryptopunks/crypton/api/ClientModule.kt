package cc.cryptopunks.crypton.api

class ClientModule(
    createApi: Api.Factory,
    override val mapException: MapException,
    override val currentApi: Api.Current = Api.Current(),
    override val apiManager: Api.Manager = ApiManager(
        createApi = createApi,
        apiCache = ApiCache()
    )
) : Api.Component