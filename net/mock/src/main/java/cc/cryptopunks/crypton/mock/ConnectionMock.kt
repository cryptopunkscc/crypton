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
    Roster.Net by RosterNetMock(state),
    UserPresence.Net by UserPresenceNetMock() {

    private val log = typedLog()

    override val connect = object : Net.Connect {
        override fun invoke() {
        }
    }
    override val disconnect = object : Net.Disconnect {
        override fun invoke() {
        }
    }
    override val interrupt = object : Net.Interrupt {
        override fun invoke() {
        }
    }
    override val isConnected = object : Net.IsConnected {
        override fun invoke(): Boolean = true
    }
    override val initOmemo = object : Net.InitOmemo {
        override fun invoke(): Boolean = runBlocking {
            delay(2000)
            state {
                omemoInitialized = true
                apiEvents.send(Net.OmemoInitialized)
                log.d("Omemo initialized")
            }
            true
        }
    }
    override val netEvents: Net.Output = object : Net.Output, () -> Flow<Api.Event> {
        override fun invoke() = state.apiEvents.asFlow()
    }
}
