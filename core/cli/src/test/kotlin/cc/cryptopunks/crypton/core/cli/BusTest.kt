package cc.cryptopunks.crypton.core.cli

import cc.cryptopunks.crypton.cli.Check
import cc.cryptopunks.crypton.cli.Commands
import cc.cryptopunks.crypton.cli.CliContext
import cc.cryptopunks.crypton.cli.command
import cc.cryptopunks.crypton.cli.named
import cc.cryptopunks.crypton.cli.param
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

val TEST_COMMANDS: Commands = mapOf(
    Unit to mapOf(
        "cmd0" to command(
            param(),
            param()
        ) { it },
        "cmd1" to mapOf(
            "subCmd0" to command(
                named("arg0"),
                named("arg1")
            ) { it }
        )
    )
)

class BusTest {

    @Test
    fun `test root command`() {
        runBlocking {
            assertEquals(
                listOf(listOf("0", "1")),
                listOf("cmd0 0 1")
                    .asFlow()
                    .translateCli(CliContext(TEST_COMMANDS))
                    .mapNotNull { it.result }
                    .toList()
            )
        }
    }

    @Test
    fun `test sub command`() {
        runBlocking {
            assertEquals(
                listOf(
                    Check.Suggest(listOf("subCmd0")),
                    Check.Suggest(listOf("arg0", "arg1")),
                    Check.Suggest(listOf("arg0")),
                    Check.Suggest(listOf("arg0")),
                    listOf("0", "1")
                ),
                listOf("cmd1", "subCmd0", "arg1 1", "arg0", "0")
                    .asFlow()
                    .translateCli(
                        CliContext(
                            TEST_COMMANDS
                        )
                    )
                    .mapNotNull { it.result }
                    .toList()
            )
        }
    }

    @Test
    fun `malformed input`() {
        runBlocking {
            assertTrue(
                listOf("uknown", "comand", "foo", "bar")
                    .asFlow()
                    .translateCli(
                        CliContext(
                            TEST_COMMANDS
                        )
                    )
                    .mapNotNull { it.result }
                    .onEach { println(it) }
                    .toList()
                    .all { it is Check.Suggest }
            )
        }
    }
}
