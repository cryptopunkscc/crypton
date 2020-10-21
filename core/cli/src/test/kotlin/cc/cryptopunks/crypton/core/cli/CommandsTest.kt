package cc.cryptopunks.crypton.core.cli

import cc.cryptopunks.crypton.cli.execute
import cc.cryptopunks.crypton.cli.prepare
import cc.cryptopunks.crypton.cli.set
import cc.cryptopunks.crypton.context.Account
import cc.cryptopunks.crypton.context.Exec
import cc.cryptopunks.crypton.context.Password
import cc.cryptopunks.crypton.context.Route
import cc.cryptopunks.crypton.context.address
import org.junit.Assert.assertEquals
import org.junit.Test

class CommandsTest {

    @Test
    fun `add account`() {
        assertEquals(
            Exec.Login(
                Account(
                    address = address("test@account.io"),
                    password = Password("pass")
                )
            ),
            context()
                .prepare()
                .set("add account test@account.io password pass")
                .execute()
        )
    }

    @Test
    fun `create account`() {
        assertEquals(
            Exec.Register(
                Account(
                    address = address("test@account.io"),
                    password = Password("pass")
                )
            ),
            context()
                .prepare()
                .set("create account test@account.io password pass")
                .execute()
        )
    }

    @Test
    fun `send message`() {
        assertEquals(
            Exec.EnqueueMessage("lorem ipsum"),
            context(
                Route.Chat(
                    "chat@address.io",
                    "test@account.io"
                )
            )
                .prepare()
                .set("send message lorem ipsum")
                .execute()
        )
    }

    @Test
    fun `navigate chat`() {
        assertEquals(
            Route.Chat(address = "asd"),
            context()
                .prepare()
                .set("navigate chat asd")
                .execute()
        )
    }
}
