package cc.cryptopunks.crypton

import cc.cryptopunks.crypton.translator.Check
import cc.cryptopunks.crypton.translator.Commands
import cc.cryptopunks.crypton.translator.Context
import cc.cryptopunks.crypton.translator.command
import cc.cryptopunks.crypton.translator.execute
import cc.cryptopunks.crypton.translator.named
import cc.cryptopunks.crypton.translator.param
import cc.cryptopunks.crypton.translator.prepare
import cc.cryptopunks.crypton.translator.set
import org.junit.Assert.assertEquals
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

class ContextTest {

    @Test
    fun `test root command`() {
        assertEquals(
            listOf("0", "1"),
            Context(TEST_COMMANDS)
                .prepare()
                .set("cmd0 0 1")
                .execute()
        )
    }

    @Test
    fun `test sub command`() {
        assertEquals(
            listOf("0", "1"),
            Context(TEST_COMMANDS)
                .prepare()
                .set("cmd1 subCmd0 arg1 1 arg0 0")
                .execute()
        )
    }

    @Test
    fun `suggest command`() {
        assertEquals(
            Check.Suggest(listOf("subCmd0")),
            Context(TEST_COMMANDS)
                .prepare()
                .set("cmd1")
                .execute()
        )
    }

    @Test
    fun `suggest arg`() {
        assertEquals(
            Check.Suggest(listOf("arg0")),
            Context(TEST_COMMANDS)
                .prepare()
                .set("cmd1 subCmd0 arg1 1")
                .execute()
        )
    }
}
