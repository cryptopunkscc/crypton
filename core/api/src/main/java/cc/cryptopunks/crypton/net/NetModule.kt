package cc.cryptopunks.crypton.net

class NetModule(
    override val createNet: Net.Factory,
    override val mapException: MapException
) : Net.Component