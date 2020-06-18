package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal fun AppScope.hasAccountsFlow(
): Flow<Boolean> = accountRepo.flowList()
    .map { it.isNotEmpty() }
    .distinctUntilChanged()
    .onEach { log.d("Has accounts - $it") }
