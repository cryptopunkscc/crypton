package cc.cryptopunks.crypton.cliv2

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

class CliContextTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

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
                result,
                Cli.Result.Any("bar,1,2,asd,dsa,lorem ipsum".split(","))
            )
        }
    }

    @Test
    fun testSetConfig() {
        Cli.Context(commands).reduce(
            Cli.Elements(
                Cli.Input.Raw(setConfig),
                Cli.Input.Raw(getConfig),
                Cli.Input.Execute
            )
        ).run {
            println(result)
            Assert.assertEquals(
                result,
                Cli.Result.Any(Cli.Config(mapOf("email" to setConfig)))
            )
        }
    }

    @Test
    fun getCommandsSuggestion() {
        Cli.Context(commands).reduce(
            Cli.Input.Raw("foo")
        ).run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Suggestion(commands.keyMap.getValue("foo")),
                result
            )
        }
    }

    @Test
    fun getParamsSuggestion() {
        Cli.Context(commands).reduce(
            Cli.Input.Raw("foo bar")
        ).run {
            println(result)
            Assert.assertEquals(
                Cli.Result.Suggestion(barCommand.params.filter {
                    it.type != Cli.Param.Type.Name
                }),
                result
            )
        }
    }
}

private const val command1 = "foo bar b 2 asd dsa a 1 lorem ipsum"
private const val setConfig = "user@address.id"
private const val getConfig = "config"

private val barCommand = command(
    name(),
    named("a"),
    named("b"),
    param(),
    param(),
    text()
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

