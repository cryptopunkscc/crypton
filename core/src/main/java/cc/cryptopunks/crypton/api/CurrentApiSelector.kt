package cc.cryptopunks.crypton.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import javax.inject.Inject

class CurrentApiSelector @Inject constructor(
    private val currentApi: Api.Current
) : () -> Flow<Api> by {
    currentApi.filterNot { it.isEmpty }
}