package cc.cryptopunks.crypton.mock.net

import cc.cryptopunks.crypton.context.Connection
import cc.cryptopunks.crypton.mock.ConnectionMock
import cc.cryptopunks.crypton.mock.MockState

class ConnectionMockFactory : Connection.Factory {
    override fun invoke(config: Connection.Config): Connection =
        ConnectionMock(
            MockState(
                config.address
            )
        )
}
