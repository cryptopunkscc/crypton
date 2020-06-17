package cc.cryptopunks.crypton.mock

import cc.cryptopunks.crypton.context.Roster
import cc.cryptopunks.crypton.mock.MockState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class MockRosterNet(
    private val state: MockState
) : Roster.Net {
    override val rosterEvents: Flow<Roster.Net.Event> get() = state.rosterEvents.consumeAsFlow()
}
