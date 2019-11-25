package cc.cryptopunks.crypton.connection

class ConnectionComponent(
    override val createConnection: Connection.Factory
) : Connection.Component