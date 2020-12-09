package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.cliv2.Cli
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge

inline fun <reified Action : Scoped<Scope>, reified Scope : CoroutineScope> feature(
    command: Cli.Command.Template? = null,
    emitter: Emitter<*>? = null,
    noinline handler: Handle<Scope, Action>
): Feature<Action, Scope> = FeatureData(
    command = command,
    handle = handler,
    emitter = emitter,
    type = Action::class.java
)

@Suppress("UNCHECKED_CAST")
fun features(vararg features: Feature<out Scoped<*>, out CoroutineScope>): Features =
    Features(features.toList() as FeatureList)

private typealias FeatureList = List<Feature<Scoped<CoroutineScope>, CoroutineScope>>
data class Features(val list: FeatureList): FeatureList by list

operator fun Features.plus(features: Features) = Features(list + features)

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

inline fun <reified Scope : CoroutineScope> emitter(
    noinline create: Scope.() -> Flow<Any>
) = Emitter(
    create = create,
    type = Scope::class.java
)

data class Emitter<Scope : CoroutineScope>(
    val type: Class<Scope>,
    val create: Scope.() -> Flow<Any>
)

inline fun <reified S: CoroutineScope>CoroutineScope.createEmitters(features: Features) =
    createEmitters(features, S::class.java)

fun CoroutineScope.createEmitters(features: Features, type: Class<*>) = features
    .mapNotNull { feature -> feature.emitter?.takeIf { emitter -> emitter.type == type } }
    .filterIsInstance<Emitter<CoroutineScope>>()
    .map { emitter -> let(emitter.create) }
    .also {
        println("emitters size: ${it.size} for scope $this")
    }
    .asFlow()
    .flattenMerge()

fun Features.createHandlers() = createHandlers {
    filter { it.handle != NonHandle }.forEach { feature ->
        unsafePlus(
            type = feature.type.kotlin,
            handle = feature.handle
        )
    }
}

private fun Features.getHandlers(): List<Handle<CoroutineScope, Scoped<CoroutineScope>>> =
    map { it.handle }.filterNot { it == NonHandle }
