package cc.cryptopunks.crypton.api.client.selector

import cc.cryptopunks.crypton.api.Client
import cc.cryptopunks.crypton.api.isEmpty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class CurrentClientSelector @Inject constructor(
    private val currentClient: Client.Current
) : () -> Flow<Client> by {
    currentClient.filterNot { it.isEmpty }
}