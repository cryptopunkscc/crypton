package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.mock.net.*
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
    Chat.Net by ChatNetMock(),
    Roster.Net by RosterNetMock(state),
    UserPresence.Net by UserPresenceNetMock() {

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
            state { apiEvents.send(Net.OmemoInitialized) }
            true
        }
    }
    override val netEvents: Net.Output = object : Net.Output, () -> Flow<Api.Event> {
        override fun invoke() = state.apiEvents.asFlow()
    }
}
