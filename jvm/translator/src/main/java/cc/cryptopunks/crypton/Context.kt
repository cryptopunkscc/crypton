package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.context.Route

data class Context(
    val route: Route = Route.Main,
    val commands: Map<Route, Map<String, Any>> = COMMANDS,
    val account: String = "",
    val state: Any = Unit,
    val result: Any? = null
)

fun Context.process(input: String): Context = set(input).run {
    anySuggestion()
        ?.let { copy(result = it) }
        ?: copy(result = execute()).run {
            if (result is Route) copy(route = result)
            else this
        }.prepare()
}


fun Context.prepare() = copy(
    state = commands[route] ?: throw IllegalArgumentException("No commands for route $route")
)

fun Context.set(string: String): Context =
    if (string.isBlank()) this
    else string.split(" ").let { strings ->
        if (strings.size != 1) set(strings)
        else copy(
            state = when (state) {

                is Command -> state.append(string)

                is Map<*, *> -> (state as Map<String, Any>)[string]
                    ?: IllegalArgumentException("Unknown command $string")

                else -> IllegalStateException("Unknown state $state")
            }
        )
    }


fun Context.set(strings: List<String>) =
    strings.fold(this) { context, string ->
        context.set(string)
    }

fun Context.anySuggestion(): Any? =
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

fun Context.execute(): Any = anySuggestion()
    ?: (state as? Command)?.run {
        run(params.map { it.value!! }).also {
            params.forEach { it.value = null }
        }
    }
    ?: IllegalStateException("Cannot execute state $state")
