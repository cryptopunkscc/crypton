package cc.cryptopunks.crypton.net

class NetModule(
    createNet: Net.Factory,
    override val mapException: MapException,
    override val currentNet: Net.Current = Net.Current(),
    override val netManager: Net.Manager = NetManager(
        createNet = createNet,
        netCache = NetCache()
    )
) : Net.Component