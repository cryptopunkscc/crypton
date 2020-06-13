package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.mock.net.*
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking

class ConnectionMock(
    private val state: MockState
) : Connection,
    Account.Net by AccountNetMock(),
    User.Net by UserNetMock(state),
    Presence.Net by PresenceNetMock(state),
    Message.Net by MessageNetMock(state),
    Chat.Net by ChatNetMock(state),
    Roster.Net by RosterNetMock(state) {

    private val log = typedLog()

    override fun connect() {}

    override fun disconnect() {}

    override fun interrupt() {}

    override fun isConnected(): Boolean = true

    override fun initOmemo(): Boolean = runBlocking {
        delay(2000)
        state {
            omemoInitialized = true
            apiEvents.send(Net.OmemoInitialized)
            log.d("Omemo initialized")
        }
        true
    }

    override fun netEvents(): Flow<Api.Event> =
        state.apiEvents.asFlow()
}
