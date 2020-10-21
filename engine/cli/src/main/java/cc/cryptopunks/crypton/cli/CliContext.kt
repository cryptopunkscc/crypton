package cc.cryptopunks.crypton.cli

typealias Commands = Map<Any, Map<String, Any>>

fun commands(vararg commands: Pair<Any, Map<String, Any>>): Commands = mapOf(*commands)

data class CliContext(
    val commands: Commands = emptyMap(),
    val scope: List<String> = emptyList(),
    val route: Any = Unit,
    val isRoute: Any.() -> Boolean = { this is Unit },
    val empty: Any.() -> Any = { this },
    val account: String = "",
    val state: Any = Unit,
    val result: Any? = null,
)

fun CliContext.process(input: String): CliContext = set(input).run {
    copy(result = execute()).run {
        if (result?.isRoute() == true) copy(route = result)
        else this
    }
}

fun CliContext.set(string: String): CliContext =
    if (string.isBlank()) this
    else string.split(" ").let { strings ->
        if (strings.size != 1) set(strings)
        else when (state) {

            is CliCommand -> state.append(string)

            is Map<*, *> -> (state as Map<String, Any>)[string]
                ?: IllegalArgumentException("Unknown command $string")

            else -> IllegalStateException("Unknown state $state")
        }.let { result ->
            when (result) {
                is Throwable -> copy(result = result)
                else -> copy(state = result)
            }
        }
    }

fun CliContext.set(strings: List<String>) =
    strings.fold(this) { context, string ->
        context.set(string)
    }

fun CliContext.execute(): Any = anySuggestion()
    ?: (state as? CliCommand)?.run {
        run(params.mapNotNull { it.value }).also {
            params.forEach { it.value = null }
        }
    }
    ?: IllegalStateException("Cannot execute state $state")


fun CliContext.anySuggestion(): Any? =
    state.let { state ->
        when (state) {

            commands[route.empty()] == state -> Check.Prepared

            is CliCommand -> when {

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

fun CliContext.prepare() = copy(
    state = commands[route.empty()]
        ?: throw IllegalArgumentException("No commands for route $route")
)

class Check {
    object Prepared
    data class Suggest(val list: List<String>)
}
