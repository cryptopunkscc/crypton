package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.*
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking

class MockConnection(
    private val state: MockState
) : Connection,
    Account.Net by MockAccountNet(),
    User.Net by MockUserNet(state),
    Presence.Net by MockPresenceNet(state),
    Message.Net by MockMessageNet(state),
    Chat.Net by MockChatNet(state),
    Roster.Net by MockRosterNet(state) {

    private val log = typedLog()

    override fun connect() {}

    override fun disconnect() {}

    override fun interrupt() {}

    override fun isConnected(): Boolean = true

    override suspend fun initOmemo() = runBlocking {
        delay(2000)
        state {
            omemoInitialized = true
            apiEvents.send(Net.OmemoInitialized)
            log.d("Omemo initialized")
        }
    }

    override fun isOmemoInitialized(): Boolean =
        state.omemoInitialized

    override fun netEvents(): Flow<Api.Event> =
        state.apiEvents.asFlow()
}
