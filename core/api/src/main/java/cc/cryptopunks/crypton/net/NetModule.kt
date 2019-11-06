package cc.cryptopunks.crypton.net

class NetModule(
    override val createNet: Net.Factory
) : Net.Component