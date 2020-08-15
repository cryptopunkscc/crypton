package cc.cryptopunks.crypton

import kotlinx.coroutines.CoroutineScope
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.reflect.KClass

interface Scope : CoroutineScope {
    val handlers: HandlerRegistry
    suspend infix fun resolve(context: Context): Pair<Scope, Any> = this to Unit }

interface Scoped<S : CoroutineScope>

@Suppress("UNCHECKED_CAST")
internal fun Scope.handlerFor(any: Any): Handle<Scope, Any>? = handlers[any::class]

interface Failure

data class InvalidAction(
    val action: String,
    val availableActions: List<String>
) : Failure {
    constructor(action: Any, availableActions: Collection<KClass<*>>) : this(
        action = action.javaClass.name,
        availableActions = availableActions.map { it.java.name }
    )
}

data class CannotResolve(
    val contextId: String
) : Failure {
    constructor(context: Context) : this(context.id)
}

data class ActionFailed(
    val action: String,
    val stackTrace: String,
    val message: String?
): Failure {
    constructor(action: Any, throwable: Throwable) : this(
        action = action.javaClass.name,
        message = throwable.message,
        stackTrace = throwable.stringStackTrace()
    )
}

fun Throwable.stringStackTrace() =
    StringWriter().also { printStackTrace(PrintWriter(it)) }.toString()
