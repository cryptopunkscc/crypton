package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Connection

class MockConnectionFactory : Connection.Factory {
    override fun invoke(config: Connection.Config): Connection =
        MockConnection(MockState(config.address))
}
