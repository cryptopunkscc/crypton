package cc.cryptopunks.crypton.context

import cc.cryptopunks.crypton.Connectable
import cc.cryptopunks.crypton.asDep
import cc.cryptopunks.crypton.cryptonContext
import cc.cryptopunks.crypton.util.logger.CoroutineLog
import cc.cryptopunks.crypton.util.logger.log
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
): RootScope = RootScope.Module(
    cryptonContext(
        dependencies,
        baseRootContext(),
    )
).apply {
    coroutineContext[Job]!!.invokeOnCompletion {
        coroutineContext.log.d { "Finish AppModule $this" }
    }
}

fun baseRootContext() = cryptonContext(
    Main(Nothing::class.java),
    Chat.NavigationId(),
    ApplicationId(),

    SessionScope.Store(),
    Clip.Board.Store(),
    Connectable.Binding.Store(),
    Account.Store(),
    Roster.Items.Store(),

    SupervisorJob(),
    Dispatchers.IO,
    CoroutineLog.Label(RootScope::class.java.simpleName),
)


fun RootScope.getSessionScope(session: Address): SessionScope =
    requireNotNull(sessions[session]) {
        "Cannot resolve SessionScope for $session\n" +
            "available sessions: ${sessions.get().keys.joinToString("\n")}"
    }

suspend fun RootScope.createSessionScope(address: Address): SessionScope =
    createSessionScope(accountRepo.get(address))

fun RootScope.createSessionScope(account: Account): SessionScope = let {
    val connectionScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val connectionConfig = Connection.Config(
        scope = connectionScope,
        account = account.address,
        password = account.password
    )

    SessionScope.Module(
        cryptonContext(
            coroutineContext,
            asDep(),
            createSessionRepo(account.address).context(),
            createConnection(connectionConfig).context(),
            baseSessionContext(Account.Name(account.address))
        )
    ).apply {
        deviceNet.setDeviceFingerprintRepo(deviceRepo)
        coroutineContext[Job]!!.invokeOnCompletion {
            connectionScope.cancel(CancellationException(it?.message))
            coroutineContext.log.d { "Finish SessionModule $account ${it.hashCode()} $it" }
        }
    }
}

fun RootScope.baseSessionContext(
    account: Account.Name,
) = cryptonContext(
    coroutineContext,
    asDep(),
    account,
    Presence.Store(),
    Address.Subscriptions.Store(),

    SupervisorJob(coroutineContext[Job]),
    newSingleThreadContext(account.address.id),
    CoroutineLog.Label(SessionScope::class.java.simpleName),
    CoroutineLog.Scope(account.address.id),
)


suspend fun SessionScope.createChatScope(
    chat: Address,
): ChatScope = ChatScope.Module(
    baseChatContext(chatRepo.get(chat))
)

fun SessionScope.baseChatContext(
    chat: Chat,
) = cryptonContext(
    coroutineContext,
    asDep(),
    chat,
    Chat.PagedMessages.Store(),

    CoroutineLog.Label(javaClass.simpleName),
    CoroutineLog.Scope(chat.address.id),
)
