package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.util.typedLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

internal class HasAccountsSelector(
    private val repo: Account.Repo
) : () -> Flow<Boolean> {
    private val log = typedLog()
    override fun invoke(): Flow<Boolean> = repo.flowList()
        .map { it.isNotEmpty() }
        .distinctUntilChanged()
        .onEach { log.d("Has accounts - $it") }
}
