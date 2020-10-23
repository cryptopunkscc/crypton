package cc.cryptopunks.crypton.cliv2

import kotlin.IllegalArgumentException

object Cli {

    data class Context(
        val commands: Commands,
        val defaults: Config = Config(),
        val execute: Execute = commands,
        val config: Config = defaults,
        val result: Result = Result.Pending
    )

    interface Execute : Element {
        class Matcher(
            val name: String,
            match: (String) -> Boolean,
        ) : (String) -> Boolean by match
    }

    interface Element

    data class Elements(val collection: Collection<Element>) : Element {
        constructor(vararg elements: Element) : this(elements.toList())
    }

    data class Config(
        val map: Map<String, Any?> = emptyMap(),
    ) : Map<String, Any?> by map,
        Element

    sealed class Result : Element {
        object Pending : Result()
        object Ready : Result()
        data class Error(val throwable: Throwable) : Result()
        data class Any(val any: kotlin.Any) : Result()
        data class Suggestion(val any: kotlin.Any) : Result()
    }

    data class Command(
        val params: List<Param>,
        val run: Context.(List<String>) -> Context,
    ) : Execute {

        data class Builder(
            val run: Context.(List<String>) -> Context,
            val empty: List<Param> = emptyList(),
            val filled: List<Param> = emptyList(),
        ) : Execute

        data class Template(
            val name: String = "",
            val params: List<Param>,
            val run: Context.(List<String>) -> Context,
        ) : Execute
    }

    data class Commands(
        val keyMap: Map<String, Execute> = emptyMap(),
        val matcherMap: Map<Execute.Matcher, Execute> = emptyMap(),
    ) : Execute

    data class Param(
        val type: Type,
        val index: Int = -1,
        val value: String? = null,
    ) {
        sealed class Type {
            data class Named(val name: String) : Type()
            object Positional : Type()
            object Text : Type()
            object Name : Type()
        }
    }

    sealed class Input : Element {
        data class Raw(val value: String) : Input()
        data class Chunk(val value: String) : Input()
        object Execute : Input()
    }

    const val DELIMITER = " "
}

// API


fun commands(args: Map<*, *>): Cli.Commands =
    commands(args.map { (k, v) -> k to v })

fun commands(vararg args: Pair<*, *>): Cli.Commands =
    commands(args.asIterable())

fun commands(args: Iterable<Pair<*, *>>): Cli.Commands =
    args.fold(Cli.Commands()) { commands, (matcher, value) ->
        val execute = when (value) {
            is Map<*, *> -> commands(value)
            is Cli.Execute -> value
            else -> throw IllegalArgumentException("$value")
        }
        commands.run {
            when (matcher) {
                is String -> copy(
                    keyMap = keyMap.plus(
                        matcher to execute
                    )
                )
                is Regex -> copy(
                    matcherMap = matcherMap.plus(
                        Cli.Execute.Matcher(matcher.toString()) {
                            matcher.matches(it)
                        } to execute
                    )
                )
                is Cli.Execute.Matcher -> copy(
                    matcherMap = matcherMap.plus(
                        matcher to execute
                    )
                )
                else -> throw IllegalArgumentException("Invalid matcher type ${matcher?.let { it::class.java }}")
            }
        }
    }

fun command(
    vararg params: Cli.Param,
    run: Cli.Context.(List<String>) -> Any = { this }
) = Cli.Command.Template(
    params = params.mapIndexed { index, param -> param.copy(index = index) },
    run = { args ->
        copy(
            result = try {
                Cli.Result.Any(run(args))
            } catch (e: Throwable) {
                Cli.Result.Error(e)
            }
        )
    }
)

fun Cli.Command.Template.raw(
    reduce: Cli.Context.(List<String>) -> Cli.Context = { this }
) = copy(run = reduce)

fun name() = Cli.Param(Cli.Param.Type.Name)
fun param(type: Cli.Param.Type = Cli.Param.Type.Positional) = Cli.Param(type)
fun named(name: String) = Cli.Param(Cli.Param.Type.Named(name))
fun text() = Cli.Param(Cli.Param.Type.Text)

fun Cli.Context.reduce(element: Cli.Element): Cli.Context =
    when (element) {
        is Cli.Elements -> reduce(element.collection)
        is Cli.Input.Raw -> reduce(element.split())
        is Cli.Input.Chunk -> reduce(execute + element)
        is Cli.Result.Ready -> reduce(Cli.Input.Execute)
        is Cli.Input.Execute -> reduce(buildCommand())
        is Cli.Command -> run(element)
        is Cli.Execute -> copy(execute = element)
        is Cli.Result -> copy(result = element)
        else -> this
    }

// PRIVATE
private fun Cli.Context.reduce(elements: Collection<Cli.Element>): Cli.Context =
    elements.fold(this, Cli.Context::reduce)

private fun Cli.Input.Raw.split(): List<Cli.Input.Chunk> =
    value.split(Cli.DELIMITER).map(Cli.Input::Chunk)


private operator fun Cli.Execute.plus(input: Cli.Input.Chunk): Cli.Element =
    when (this) {
        is Cli.Commands -> when (val element = plus(input)) {
            is Cli.Command.Template -> element.builder(input)
            else -> element
        }
        is Cli.Command.Builder -> plus(input)
        else -> throw IllegalStateException("Cannot handle plus on $this for given $input")
    }.let { element ->
        when (element) {
            is Cli.Command.Builder -> Cli.Elements(
                element,
                if (element.empty.isEmpty()) Cli.Result.Ready
                else Cli.Result.Suggestion(element.empty)
            )
            is Cli.Commands -> Cli.Elements(
                element,
                Cli.Result.Suggestion(element)
            )
            else -> element
        }
    }

private operator fun Cli.Commands.plus(
    input: Cli.Input.Chunk,
): Cli.Element =
    keyMap[input.value]
        ?: matcherMap.filterKeys { match -> match(input.value) }.values.firstOrNull()
        ?: Cli.Result.Suggestion(keyMap.keys.toList() + matcherMap.keys.map(Cli.Execute.Matcher::name))

private operator fun Cli.Command.Builder.plus(
    arg: Cli.Input.Chunk,
): Cli.Command.Builder =
    run {
        empty.firstOrNull { param ->
            when (param.type) {
                is Cli.Param.Type.Named -> param.type.name == arg.value
                is Cli.Param.Type.Positional -> true
                is Cli.Param.Type.Text -> empty.size == 1
                is Cli.Param.Type.Name -> false
            }
        } ?: filled.lastOrNull { param ->
            param.type == Cli.Param.Type.Text
        }
    }?.let { param ->
        when (param.type) {
            is Cli.Param.Type.Named -> copy(
                empty = listOf(param.copy(type = Cli.Param.Type.Positional)) + (empty - param)
            )
            is Cli.Param.Type.Positional -> copy(
                empty = empty.filter { it.index != param.index },
                filled = filled + param.copy(value = arg.value)
            )
            is Cli.Param.Type.Text -> copy(
                empty = empty.dropLastWhile { it.type == Cli.Param.Type.Text },
                filled = filled.dropLastWhile { it.type == Cli.Param.Type.Text } + param.copy(
                    value = param.value?.let { it + Cli.DELIMITER + arg.value } ?: arg.value
                )
            )
            else -> this
        }
    } ?: throw IllegalStateException("No more chunks!!!")


private fun Cli.Command.Template.builder(chunk: Cli.Input.Chunk) =
    Cli.Command.Builder(
        empty = params.filter { it.type !is Cli.Param.Type.Name },
        filled = params.mapNotNull { param ->
            if (param.type !is Cli.Param.Type.Name) null
            else param.copy(value = chunk.value)
        },
        run = run,
    )

private fun Cli.Context.buildCommand(): Cli.Command =
    run {
        check(execute is Cli.Command.Builder)
        Cli.Command(
            params = execute.stringParams(),
            run = execute.run
        )
    }

private fun Cli.Command.Builder.stringParams(): List<Cli.Param> =
    filled.sortedBy(Cli.Param::index)

private fun Cli.Context.run(command: Cli.Command): Cli.Context =
    command.run { run(params.mapNotNull(Cli.Param::value)) }
