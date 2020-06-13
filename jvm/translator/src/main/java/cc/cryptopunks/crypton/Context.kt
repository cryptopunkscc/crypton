package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Route

data class Context(
    val route: Route = Route.SetAccount,
    val commands: Map<Route, Map<String, Any>> = COMMANDS,
    val account: String = "",
    val state: Any = Unit
)

fun Context.prepare() =
    copy(state = commands[route] ?: throw IllegalArgumentException("No commands for route $route"))

fun Context.set(string: String): Context =
    string.split(" ").let { strings ->
        if (strings.size != 1) set(strings)
        else copy(
            state = when (state) {

                is Command -> state.append(string)

                is Map<*, *> -> (state as Map<String, Any>)[string]
                    ?: throw IllegalArgumentException("Unknown command $string")

                else -> throw IllegalStateException("Unknown state $state")
            }
        )
    }


fun Context.set(strings: List<String>) =
    strings.fold(this) { context, string ->
        context.set(string)
    }

fun Context.validate() =
    state.let { state ->
        when (state) {

            COMMANDS[route] == state -> Check.Prepared

            is Command -> when {
                state.canExecute() -> null
                else -> Check.Suggest(
                    state.emptyParams()
                        .filterIsInstance<Input.Named>()
                        .mapNotNull(Input.Named::name)
                )
            }

            is Map<*, *> -> Check.Suggest(
                state.keys.filterIsInstance<String>()
            )

            else -> null
        }
    }

class Check {
    object Prepared
    data class Suggest(val list: List<String>)
}

fun Context.execute() = validate() ?: (state as? Command)
    ?.run { run(params.map { it.value!! }) }
?: throw IllegalStateException("Cannot execute state $state")
