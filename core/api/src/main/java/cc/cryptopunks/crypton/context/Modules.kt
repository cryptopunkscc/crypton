package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.ScopeElement
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.logv2.log
import cc.cryptopunks.crypton.logv2.d
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.newSingleThreadContext
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext

fun createRootScope(
    dependencies: CoroutineContext,
): RootScope = CoroutineScope(
    cryptonContext(
        dependencies,
        baseRootContext(),
    )
).apply {
    coroutineContext[Job]!!.invokeOnCompletion {
        log.d { "Finish AppModule $this" }
    }
}

fun baseRootContext() = cryptonContext(
    RootScopeTag,
    CoroutineLog.Label("RootScope"),
    Main(Nothing::class.java),
    Chat.NavigationId(),
    ApplicationId(),

    SessionStore(),
    Clip.Board.Store(),
    Message.Consumer.Store(),
    Account.Store(),
    Roster.Items.Store(),

    SupervisorJob(),
//    newSingleThreadContext("RootScope"),
    Dispatchers.IO,
    CoroutineLog.Label(RootScope::class.java.simpleName),
)


fun RootScope.getSessionScope(session: Address): SessionScope =
    requireNotNull(sessions[session]) {
        "Cannot resolve SessionScope for $session\n" +
            "available sessions: ${sessions.get().keys.joinToString("\n")}"
    }

suspend fun createSessionScope(scope: RootScope, address: Address): SessionScope =
    createSessionScope(scope, scope.accountRepo.get(address))

fun createSessionScope(
    scope: RootScope,
    account: Account,
): SessionScope = Connection.Config(
    scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    account = account.address,
    password = account.password
).let { connectionConfig ->
    CoroutineScope(
        cryptonContext(
            scope.createSessionRepo(account.address).context(),
            scope.createConnection(connectionConfig).context(),
            baseSessionContext(scope, Account.Name(account.address))
        )
    ).apply {
        this.deviceNet.setDeviceFingerprintRepo(this.deviceRepo)
        this.coroutineContext[Job]!!.invokeOnCompletion {
            connectionConfig.scope.cancel(CancellationException(it?.message))
            log.d { "Finish SessionModule $account ${it.hashCode()} $it" }
        }
    }
}

fun baseSessionContext(
    rootScope: RootScope,
    account: Account.Name,
) = cryptonContext(
    rootScope.coroutineContext,
    rootScope.asDep(RootScopeTag),
    account,
    SessionScopeTag,
    ScopeElement(account.address.id),
    Presence.Store(),
    Address.Subscriptions.Store(),
    SupervisorJob(rootScope.coroutineContext[Job]),
    newSingleThreadContext(account.address.id),
    CoroutineLog.Label("SessionScope"),
    CoroutineLog.Scope(account.address.id),
)


suspend fun createChatScope(
    sessionScope: SessionScope,
    chat: Address,
): ChatScope = CoroutineScope(
    baseChatContext(
        sessionScope,
        sessionScope.chatRepo.get(chat)
    )
)

fun baseChatContext(
    sessionScope: SessionScope,
    chat: Chat,
) = cryptonContext(
    sessionScope.coroutineContext,
    sessionScope.asDep(SessionScopeTag),
    chat,
    ChatScopeTag,
    ScopeElement(chat.address.id),
    Chat.PagedMessages.Store(),
    CoroutineLog.Scope(chat.address.id),
    CoroutineLog.Label("ChatScope"),
)
