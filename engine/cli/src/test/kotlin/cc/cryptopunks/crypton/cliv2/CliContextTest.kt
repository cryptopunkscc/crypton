package cc.cryptopunks.crypton.cliv2

import org.junit.Assert
import org.junit.Test

class CliContextTest {

    @Test
    fun testSuccess() {
        Cli.Context(commands).reduce(
            Cli.Elements(
                Cli.Input.Raw(command1),
                Cli.Input.Execute
            )

        ).run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Return("bar,1,2,true,asd,dsa,lorem ipsum".split(",")),
                result,
            )
        }
    }

    @Test
    fun testSetConfig() {
        Cli.Context(commands).reduce("$setConfig $getConfig").run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Return(Cli.Config(mapOf("email" to setConfig))),
                result,
            )
        }
    }

    @Test
    fun getCommandsSuggestion() {
        Cli.Context(commands).reduce("foo").run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Suggestion(commands.keyMap.getValue("foo")),
                result
            )
        }
    }

    @Test
    fun getParamsSuggestion() {
        Cli.Context(commands).reduce("foo bar").run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Suggestion(barCommand.params.filter {
                    it.type != Cli.Param.Type.Name
                }),
                result
            )
        }
    }

    @Test
    fun omitOptionalParameters() {
        Cli.Context(commands).reduce(
            Cli.Input.Raw("foo bar asd dsa lorem ipsum")
        ).run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Return("bar,,,false,asd,dsa,lorem ipsum".split(",")),
                result
            )
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun failOnInvalidParams() {
        command(
            text(),
            param(),
            text()
        )
    }
}

private const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

private const val command1 = "foo bar b 2 o asd dsa a 1 lorem ipsum"
private const val setConfig = "user@address.id"
private const val getConfig = "config"

private val barCommand = command(
    name(),
    named("a").optional(),
    named("b").optional(),
    option("o").optional().copy(value = false),
    param(),
    param(),
    text(),
) { it }

private val commands = commands(
    "foo" to mapOf(
        "bar" to barCommand
    ),
    "config" to command {
        config
    },
    Regex(EMAIL_REGEX) to command(
        name()
    ).raw { (email) ->
        copy(
            execute = commands,
            config = Cli.Config(
                mapOf("email" to email)
            ),
        )
    }
)

