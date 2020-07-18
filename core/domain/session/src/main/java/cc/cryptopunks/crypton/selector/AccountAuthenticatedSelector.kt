package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.SessionScope
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

fun SessionScope.accountAuthenticatedFlow() = flowOf(
    flow { if (isAuthenticated()) emit(Account.Authenticated(false)) },
    netEvents().filterIsInstance()
).flattenMerge().debounce(1000).onEach {
    log.d("Account authenticated, $it")
}
