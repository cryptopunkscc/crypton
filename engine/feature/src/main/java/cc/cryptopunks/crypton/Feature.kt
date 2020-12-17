package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import kotlinx.coroutines.CoroutineScope

inline fun <reified Action : Scoped<Scope>, reified Scope : CoroutineScope> feature(
    command: Cli.Command.Template? = null,
    emitter: Emitter<*>? = null,
    noinline handler: Handle<Scope, Action>,
): Feature<Action, Scope> = FeatureData(
    command = command,
    handle = handler,
    emitter = emitter,
    type = Action::class.java
)

interface Feature<Action : Scoped<Scope>, Scope : CoroutineScope> {
    val command: Cli.Command.Template? get() = null
    val emitter: Emitter<*>? get() = null
    val handle: Handle<Scope, Action>
    val type: Class<Action>
}

data class FeatureData<Action : Scoped<Scope>, Scope : CoroutineScope>(
    override val command: Cli.Command.Template? = null,
    override val emitter: Emitter<*>? = null,
    override val handle: Handle<Scope, Action>,
    override val type: Class<Action>,
) : Feature<Action, Scope>

private fun Features.getHandlers(): List<Handle<CoroutineScope, Scoped<CoroutineScope>>> =
    map { it.handle }.filterNot { it == NonHandle }
