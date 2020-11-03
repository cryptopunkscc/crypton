package cc.cryptopunks.crypton.selector

import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.SessionScope
import cc.cryptopunks.crypton.util.ext.bufferedThrottle
import cc.cryptopunks.crypton.util.logger.log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

fun SessionScope.accountAuthenticatedFlow(): Flow<Account.Authenticated> = flowOf(
    flow { if (isAuthenticated()) emit(Account.Authenticated(false)) },
    netEvents().filterIsInstance()
).flattenMerge().bufferedThrottle(1000).flatMapConcat {
    it.toSet().asFlow()
}.onEach {
    log.v { "Account authenticated $it" }
}
