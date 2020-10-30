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

    interface Element {
        object Empty : Element
    }

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
        data class Return(val value: Any) : Result()
        data class Suggestion(val value: Any) : Result()
    }

    data class Command(
        val params: List<Param>,
        val run: Context.(List<Any?>) -> Context,
    ) : Execute {

        data class Builder(
            val run: Context.(List<Any?>) -> Context,
            val empty: List<Param> = emptyList(),
            val filled: List<Param> = emptyList(),
        ) : Execute

        data class Template(
            val description: String? = null,
            val params: List<Param>,
            val run: Context.(List<Any?>) -> Context,
        ) : Execute
    }

    data class Commands(
        val description: String? = null,
        val keyMap: Map<String, Execute> = emptyMap(),
        val matcherMap: Map<Execute.Matcher, Execute> = emptyMap(),
    ) : Execute

    data class Param(
        val type: Type,
        val name: String = "value",
        val description: String? = null,
        val index: Int = -1,
        val value: Any? = null,
        val optional: Boolean = false
    ) {
        sealed class Type {
            data class Named(val name: String) : Type()
            data class Config(val name: String) : Type()
            data class Option(val on: String, val off: String?) : Type()
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
    const val RETURN = ";"
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
    description: String? = null,
    run: Cli.Context.(List<String>) -> Any = { this }
) = Cli.Command.Template(
    description = description,
    params = params
        .mapIndexed { index, param -> param.copy(index = index) }
        .validate(),
    run = { args ->
        copy(
            result = try {
                Cli.Result.Return(run(args.map { it?.toString() ?: "" }))
            } catch (e: Throwable) {
                Cli.Result.Error(e)
            }
        )
    }
)

private fun List<Cli.Param>.validate() = apply {
    require(
        filter { it.type == Cli.Param.Type.Text }.all { it.index == size - 1 }
    ) { "Only the last parameter can have a type text." }
}

fun Cli.Command.Template.raw(
    reduce: Cli.Context.(List<Any?>) -> Cli.Context = { this }
) = copy(run = reduce)

fun name() = Cli.Param(Cli.Param.Type.Name)
fun param(type: Cli.Param.Type = Cli.Param.Type.Positional) = Cli.Param(type)
fun named(name: String) = Cli.Param(Cli.Param.Type.Named(name))
fun option(on: String, off: String? = null) =
    Cli.Param(Cli.Param.Type.Option(on, off), value = false)

fun text() = Cli.Param(Cli.Param.Type.Text, optional = true)

fun Cli.Param.optional() = copy(optional = true)

fun Array<out Any>.joinArgs() = joinToString(" ")

fun Cli.Context.reduce(input: String) = reduce(Cli.Input.Raw(input).also { println(input) })

fun Cli.Context.reduce(element: Cli.Element): Cli.Context = when (element) {
    is Cli.Elements -> reduce(element.collection)
    is Cli.Input.Raw -> reduce(element.split())
    is Cli.Input.Chunk -> prepareIfNeeded().run { reduce(plus(element)) }
    is Cli.Result.Ready -> reduce(Cli.Input.Execute)
    is Cli.Input.Execute -> reduce(buildCommand())
    is Cli.Command -> run(element)
    is Cli.Execute -> copy(execute = element)
    is Cli.Result -> copy(result = element)
    else -> this
}


fun Cli.Context.prepareIfNeeded(): Cli.Context =
    when (result) {
        is Cli.Result.Return,
        is Cli.Result.Error -> prepare()
        else -> this
    }

fun Cli.Context.prepare() = copy(
    execute = commands,
    result = Cli.Result.Pending
)

// PRIVATE
private fun Cli.Context.reduce(elements: Collection<Cli.Element>): Cli.Context =
    elements.fold(this, Cli.Context::reduce)

private fun Cli.Input.Raw.split(): List<Cli.Input> =
    value.split(Cli.RETURN)
        .map { command ->
            command.split(Cli.DELIMITER)
                .map(Cli.Input::Chunk)
                .plus(Cli.Input.Execute)
        }
        .flatten()


private fun Cli.Context.plus(input: Cli.Input.Chunk): Cli.Element =
    when (execute) {
        is Cli.Commands -> when (val element = execute.plus(input)) {
            is Cli.Command.Template -> builder(element, input)
            else -> element
        }
        is Cli.Command.Builder -> execute.plus(input)
        else -> throw IllegalStateException("Cannot handle plus on $this for given $input")
    }.let { element ->
        when (element) {
            is Cli.Command.Builder -> Cli.Elements(
                element,
                if (element.isReady()) Cli.Result.Ready
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
        ?: this

private operator fun Cli.Command.Builder.plus(
    arg: Cli.Input.Chunk,
): Cli.Command.Builder =
    run {
        empty.firstOrNull { param ->
            when (param.type) {
                is Cli.Param.Type.Named -> param.type.name == arg.value
                is Cli.Param.Type.Option -> param.type.on == arg.value || param.type.off == arg.value
                is Cli.Param.Type.Positional -> true
                is Cli.Param.Type.Text -> empty.filter { it.type !is Cli.Param.Type.Named && it.type !is Cli.Param.Type.Option }.size == 1
                else -> throw IllegalStateException()
            }
        } ?: filled.lastOrNull { param ->
            param.type == Cli.Param.Type.Text
        }
    }?.let { param ->
        when (param.type) {
            is Cli.Param.Type.Named -> copy(
                empty = listOf(param.copy(type = Cli.Param.Type.Positional)) + (empty - param)
            )
            is Cli.Param.Type.Option -> copy(
                empty = empty.filter { it.index != param.index },
                filled = filled + param.copy(value = arg.value == param.type.on)
            )
            is Cli.Param.Type.Positional -> copy(
                empty = empty.filter { it.index != param.index },
                filled = filled + param.copy(value = arg.value)
            )
//            is Cli.Param.Type.Text -> copy(
//                empty = empty.dropLastWhile { it.type == Cli.Param.Type.Text },
//                filled = filled.dropLastWhile { it.type == Cli.Param.Type.Text } + param.copy(
//                    value = param.value?.let { it.toString() + Cli.DELIMITER + arg.value }
//                        ?: arg.value
//                )
//            )
            is Cli.Param.Type.Text -> copy(
                empty = empty.dropLastWhile { it.type == Cli.Param.Type.Text } + param.copy(
                    value = param.value?.let { it.toString() + Cli.DELIMITER + arg.value }
                        ?: arg.value
                )
            )
            else -> this
        }
    } ?: this


private fun Cli.Context.builder(template: Cli.Command.Template, chunk: Cli.Input.Chunk) =
    Cli.Command.Builder(
        empty = template.params.filter { it.type !is Cli.Param.Type.Name },
        filled = template.params.mapNotNull { param ->
            when (param.type) {
                is Cli.Param.Type.Name -> param.copy(value = chunk.value)
                is Cli.Param.Type.Config -> param.copy(value = config[param.name])
                else -> null
            }
        },
        run = template.run,
    )

private fun Cli.Context.buildCommand(): Cli.Element =
    run {
        when (execute) {
            is Cli.Command.Builder -> if (execute.isEnough().not())
                Cli.Result.Suggestion(execute.empty) else
                Cli.Command(
                    params = execute.params,
                    run = execute.run
                )
            is Cli.Commands -> Cli.Result.Suggestion(execute)
            is Cli.Command -> Cli.Element.Empty
            else -> execute
        }
    }

private val Cli.Command.Builder.params: List<Cli.Param>
    get() = (filled + empty).sortedBy(Cli.Param::index)

private fun Cli.Command.Builder.isReady(): Boolean =
    empty.isEmpty()

private fun Cli.Command.Builder.isEnough(): Boolean =
    empty.isEmpty() || empty.all { it.optional }

private fun Cli.Context.run(command: Cli.Command): Cli.Context =
    command.run { run(params.map(Cli.Param::value)) }
